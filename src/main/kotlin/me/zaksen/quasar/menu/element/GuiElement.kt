package me.zaksen.quasar.menu.element

import me.zaksen.quasar.menu.MenuStructure
import me.zaksen.quasar.menu.display.Layout
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryPreClickEvent

interface GuiElement {
    /** Element x position */
    var x: Int
    /** Element y position */
    var y: Int
    /** Element x size */
    val width: Int
    /** Element y size */
    val height: Int

    /** Will be processed when added to menu */
    fun preInit(menu: MenuStructure) {}
    /** Will be processed when all elements should initialize (when menu was open) */
    fun initFor(menu: MenuStructure, player: Player) {}
    /** Will be processed when viewer want to do click */
    fun onClick(menu: MenuStructure, event: InventoryPreClickEvent) {}

    /** Will be processed when menu need to build display for this element */
    fun rebuildDisplay(menu: MenuStructure, layout: Layout)

    // Utilities
    /** @return Pair with position from index (x as first, y as second) */
    fun indexToPos(index: Int): Pair<Int, Int> {
        val y: Int = index / width
        val x: Int = index - (width * y)
        return Pair(x, y)
    }
}