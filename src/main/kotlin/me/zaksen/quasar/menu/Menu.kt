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

// TODO - Add display rebuild processing
open class Menu(private val type: MenuType, private val allowClose: Boolean = true): MenuStructure {

    constructor(width: Int, height: Int, allowClose: Boolean = true) : this(MenuType.bySize(width, height), allowClose)

    private val viewers: MutableSet<Player> = mutableSetOf()
    private val inventory = Inventory(type.invType, Component.empty())
    private val subElements: MutableSet<GuiElement> = mutableSetOf()

    override fun processClick(event: InventoryPreClickEvent) {
        val pos = indexToPos(event.slot)
        val element = getElementByPos(pos.first, pos.second)
        element?.onClick(event)
    }

    override fun setElement(index: Int, element: GuiElement) {
        val pos = indexToPos(index)
        if(checkOverlaps(pos.first, pos.second, element)) throw IllegalArgumentException("New element overlap other one")
        element.x = pos.first
        element.y = pos.second
        subElements.add(element)
    }

    override fun setElement(x: Int, y: Int, element: GuiElement) {
        if(checkOverlaps(x, y, element)) throw IllegalArgumentException("New element overlap other one")
        element.x = x
        element.y = y
        subElements.add(element)
        element.preInit(this)
    }

    override fun addElement(element: GuiElement) {
        if(checkOverlaps(element.x, element.y, element)) throw IllegalArgumentException("New element overlap other one")
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
            subElements.forEach { it.initFor(player) }
        }
        update()
    }

    override fun update(special: GuiElement?) {
        if(special != null) {
            val layout = layoutFor(special)
            special.rebuildDisplay(layout)
            layout.setup(inventory)
        } else {
            subElements.forEach {
                val layout = layoutFor(it)
                it.rebuildDisplay(layout)
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
            val endPos = Pair(it.x + it.width, it.y + it.height)

            if(x >= it.x && x <= endPos.first && y >= it.y && y <= endPos.second) {
                return it
            }
        }
        return null
    }

    /**
     * @return true if new element overlap other sub element
     */
    protected fun checkOverlaps(x: Int, y: Int, element: GuiElement): Boolean {
        val endPos = Pair(x + element.width, y + element.height)

        if(endPos.first >= type.gridSize.first  || endPos.second >= type.gridSize.second) {
            return true
        }

        subElements.forEach {
            val subEndPos = Pair(it.x + it.width, it.y + it.height)

            // Check start x overlaps
            if(x >= it.x && x <= subEndPos.first) {
                return true
            }

            // Check start y overlaps
            if(y >= it.y && y <= subEndPos.second) {
                return true
            }

            // Check end x overlaps
            if(endPos.first >= subEndPos.first && endPos.first <= subEndPos.first) {
                return true
            }

            // Check end y overlaps
            if(endPos.second >= subEndPos.second && endPos.second <= subEndPos.second) {
                return true
            }
        }

        return false
    }

    /** @return Pair with position from index (x as first, y as second) */
    @Throws(IllegalArgumentException::class)
    fun indexToPos(index: Int): Pair<Int, Int> {
        val maxIndex = (type.gridSize.first * type.gridSize.second) - 1

        if(index > maxIndex) {
            throw IllegalArgumentException("Index should be in range (0, ${maxIndex})")
        }

        val y = index % type.gridSize.first
        val x = index - (type.gridSize.first * y)
        return Pair(x, y)
    }
}