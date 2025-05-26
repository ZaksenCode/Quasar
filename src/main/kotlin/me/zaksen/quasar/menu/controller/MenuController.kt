package me.zaksen.quasar.menu.controller

import me.zaksen.quasar.menu.MenuStructure
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import java.util.UUID

/**
 * Controls how menus are displayed and execute their logic.
 * Any MenuStructure opened by a player must be registered here.
 */
object MenuController {
    private val structureByPlayer: MutableMap<UUID, MenuStructure> = mutableMapOf()

    /** Add default listeners for passed node, that controller may process logic
     * @param node - where events are processed. In most cases probably will be `MinecraftServer.getGlobalEventHandler()`
     * @see MinecraftServer
     * */
    fun initHandlers(node: EventNode<Event> = MinecraftServer.getGlobalEventHandler()) {
        // Process clicking
        node.addListener(InventoryPreClickEvent::class.java) { event ->
            structureByPlayer[event.entity.uuid]?.processClick(event)
        }
        // Process menu closing (or not closing)
        node.addListener(InventoryCloseEvent::class.java) { event ->
            val menu = structureByPlayer[event.entity.uuid] ?: return@addListener
            if(!menu.allowClose()) menu.cancelClose(event)
            else structureByPlayer.remove(event.player.uuid)
        }
        // Process menu removing, when player disconnect
        node.addListener(PlayerDisconnectEvent::class.java) { event ->
            structureByPlayer.remove(event.player.uuid)
        }
    }

    /** return true if player can open other menu */
    fun holdMenu(menu: MenuStructure, viewer: Player): Boolean {
        if(!structureByPlayer.containsKey(viewer.uuid)) {
            structureByPlayer[viewer.uuid] = menu
            return true
        }
        return false
    }

    /** force set specialized menu for player */
    fun forceHoldMenu(menu: MenuStructure, viewer: Player) {
        structureByPlayer[viewer.uuid] = menu
    }

    /** remove player any menu */
    fun closeMenu(viewer: Player) {
        structureByPlayer.remove(viewer.uuid)
        viewer.closeInventory()
    }
}