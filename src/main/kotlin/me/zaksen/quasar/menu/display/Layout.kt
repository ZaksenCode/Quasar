package me.zaksen.quasar.menu.display

import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

/** Is a relative representation of a part of the inventory.
 *  It should contain only those slots that fit within its dimensions.
 */
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
     * After setup all layout menu will update, so you shouldn't send packets with setItemStack().
     * */
    fun setup(inventory: Inventory)
}