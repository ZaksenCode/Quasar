package me.zaksen.quasar.menu

import me.zaksen.quasar.menu.controller.MenuController
import me.zaksen.quasar.menu.display.Layout
import me.zaksen.quasar.menu.display.SimpleLayout
import me.zaksen.quasar.menu.element.GuiElement
import me.zaksen.quasar.menu.element.StackElement
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack
import java.util.function.Consumer

open class Menu(private val type: MenuType, private val allowClose: Boolean = true): MenuStructure {

    constructor(width: Int, height: Int, allowClose: Boolean = true) : this(MenuType.bySize(width, height), allowClose)

    private val viewers: MutableSet<Player> = mutableSetOf()
    private val inventory = Inventory(type.invType, Component.empty())
    private val subElements: MutableSet<GuiElement> = mutableSetOf()

    override fun processClick(event: InventoryPreClickEvent) {
        val pos = indexToPos(event.slot)
        val element = getElementByPos(pos.first, pos.second)
        element?.onClick(this, event)
    }

    override fun setElement(index: Int, element: GuiElement) {
        val pos = indexToPos(index)
        element.x = pos.first
        element.y = pos.second
        subElements.add(element)
    }

    override fun setElement(x: Int, y: Int, element: GuiElement) {
        element.x = x
        element.y = y
        subElements.add(element)
        element.preInit(this)
    }

    override fun addElement(element: GuiElement) {
        subElements.add(element)
        element.preInit(this)
    }

    override fun allowClose(): Boolean {
        return allowClose
    }

    /** Shortcut to make adding stacks easier */
    fun setStack(index: Int, stack: ItemStack, onClick: Consumer<InventoryPreClickEvent> = Consumer {  }) {
        setElement(index, StackElement(stack, onClick))
    }

    /** Shortcut to make adding stacks easier */
    fun setStack(x: Int, y: Int, stack: ItemStack, onClick: Consumer<InventoryPreClickEvent> = Consumer {  }) {
        setElement(x, y, StackElement(stack, onClick))
    }

    override fun cancelClose(event: InventoryCloseEvent) {
        event.newInventory = inventory
    }

    override fun addViewer(player: Player) {
        if(MenuController.holdMenu(this, player)) {
            viewers.add(player)
            player.openInventory(inventory)
            subElements.forEach { it.initFor(this, player) }
        }
        update()
    }

    override fun update(special: GuiElement?) {
        if(special != null) {
            val layout = layoutFor(special)
            special.rebuildDisplay(this, layout)
            layout.setup(inventory)
        } else {
            subElements.forEach {
                val layout = layoutFor(it)
                it.rebuildDisplay(this, layout)
                layout.setup(inventory)
            }
        }

        inventory.update()
    }

    override fun close(closeFor: Player?) {
        if(closeFor == null) {
            viewers.forEach {
                MenuController.closeMenu(it)
            }
            viewers.clear()
            return
        }

        MenuController.closeMenu(closeFor)
        viewers.remove(closeFor)
    }

    override fun setTitle(title: Component) {
        inventory.title = title
    }

    protected fun layoutFor(element: GuiElement): Layout {
        return SimpleLayout(element.x, element.y, element.width, element.height, type.gridSize.first)
    }

    /** @return Element that exists on this position, null if element didn't exist */
    protected fun getElementByPos(x: Int, y: Int): GuiElement? {
        subElements.forEach {
            val endPos = Pair(it.x + (it.width - 1), it.y + (it.height - 1))
            if(x >= it.x && x <= endPos.first && y >= it.y && y <= endPos.second) {
                return it
            }
        }
        return null
    }

    /** @return Pair with position from index (x as first, y as second) */
    fun indexToPos(index: Int): Pair<Int, Int> {
        val y: Int = index / type.gridSize.first
        val x: Int = index - (type.gridSize.first * y)
        return Pair(x, y)
    }
}