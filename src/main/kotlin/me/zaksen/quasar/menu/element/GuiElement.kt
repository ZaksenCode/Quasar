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
    fun initFor(player: Player) {}
    /** Will be processed when viewer want to do click */
    fun onClick(event: InventoryPreClickEvent) {}

    /** Will be processed when menu need to build display for this element */
    fun rebuildDisplay(layout: Layout)

    // Utilities
    /** @return Pair with position from index (x as first, y as second) */
    @Throws(IllegalArgumentException::class)
    fun indexToPos(index: Int): Pair<Int, Int> {
        val maxIndex = (width * height) - 1

        if(index > maxIndex) {
            throw IllegalArgumentException("Index should be in range (0, ${maxIndex})")
        }

        val y = index % width
        val x = index - (width * y)
        return Pair(x, y)
    }
}