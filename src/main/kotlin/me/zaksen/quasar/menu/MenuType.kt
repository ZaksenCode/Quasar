package me.zaksen.quasar.menu

import net.minestom.server.inventory.InventoryType

enum class MenuType(
    val invType: InventoryType,
    val gridSize: Pair<Int, Int>
) {
    CHEST_1_ROW(InventoryType.CHEST_1_ROW, Pair(9, 1)),
    CHEST_2_ROW(InventoryType.CHEST_2_ROW, Pair(9, 2)),
    CHEST_3_ROW(InventoryType.CHEST_3_ROW, Pair(9, 3)),
    CHEST_4_ROW(InventoryType.CHEST_4_ROW, Pair(9, 4)),
    CHEST_5_ROW(InventoryType.CHEST_5_ROW, Pair(9, 5)),
    CHEST_6_ROW(InventoryType.CHEST_6_ROW, Pair(9, 6)),
    WINDOW_3X3(InventoryType.WINDOW_3X3, Pair(3, 3));

    companion object {
        fun bySize(x: Int, y: Int): MenuType {
            entries.forEach {
                if(it.gridSize.first == x && it.gridSize.second == y) {
                    return it
                }
            }
            return CHEST_3_ROW
        }
    }
}