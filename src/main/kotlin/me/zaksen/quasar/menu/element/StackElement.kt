package me.zaksen.quasar.menu.element

import me.zaksen.quasar.menu.MenuStructure
import me.zaksen.quasar.menu.display.Layout
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.item.ItemStack
import java.util.function.Consumer

/** Implementation of gui element for item stack */
class StackElement(
    private val stack: ItemStack,
    override var x: Int = 0,
    override var y: Int = 0,
    private val onClick: Consumer<InventoryPreClickEvent> = Consumer {  }
): GuiElement {

    constructor(stack: ItemStack, onClick: Consumer<InventoryPreClickEvent> = Consumer { }): this(stack, 0, 0, onClick)

    override val width: Int = 1
    override val height: Int = 1

    override fun onClick(menu: MenuStructure, event: InventoryPreClickEvent) {
        onClick.accept(event)
    }

    override fun rebuildDisplay(menu: MenuStructure, layout: Layout) {
        layout.set(0, stack)
    }
}