package me.zaksen.quasar.menu.display

import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

interface Layout {
    /** Fill all layout slots with specialized item
     * @param stack item that should fill layout
     * */
    fun fill(stack: ItemStack)
    /** Set stack by index */
    fun set(index: Int, stack: ItemStack)
    /** Set stack by position */
    fun set(x: Int, y: Int, stack: ItemStack)
    /** Should implement logic for filling inventory with set items
     * After setup all layout menu will update automate, so you shouldn't send packets with setItemStack().
     * */
    fun setup(inventory: Inventory)
}