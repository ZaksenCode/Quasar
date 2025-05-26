package me.zaksen.quasar.menu.display

import net.minestom.server.inventory.Inventory
import net.minestom.server.item.ItemStack

class SimpleLayout(
    private val x: Int,
    private val y: Int,
    private val width: Int,
    private val height: Int,
    private val inventoryWidth: Int
): Layout {
    private val stacks: MutableMap<Int, ItemStack> = mutableMapOf()

    override fun fill(stack: ItemStack) {
        for(sX in x..<(x + width)) {
            for(sY in y..<(y + height)) {
                set(sX, sY, stack)
            }
        }
    }

    override fun set(index: Int, stack: ItemStack) {
        stacks[(this.x + (this.y * inventoryWidth)) + index] = stack
    }

    override fun set(x: Int, y: Int, stack: ItemStack) {
        stacks[(this.x + x) + (this.y + (y * inventoryWidth))] = stack
    }

    override fun setup(inventory: Inventory) {
        stacks.forEach {
            inventory.setItemStack(it.key, it.value, false)
        }
    }
}