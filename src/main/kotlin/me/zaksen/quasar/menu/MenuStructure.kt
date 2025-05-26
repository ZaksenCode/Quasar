package me.zaksen.quasar.menu

import me.zaksen.quasar.menu.element.GuiElement
import net.kyori.adventure.text.Component
import net.minestom.server.entity.Player
import net.minestom.server.event.inventory.InventoryCloseEvent
import net.minestom.server.event.inventory.InventoryPreClickEvent

interface MenuStructure {
    /** Process elements clicks */
    fun processClick(event: InventoryPreClickEvent)

    /** Set element in menu by index */
    fun setElement(index: Int, element: GuiElement)
    /** Set element in menu by position */
    fun setElement(x: Int, y: Int, element: GuiElement)
    /** Add element to menu*/
    fun addElement(element: GuiElement)

    /** If false, then menu can be closed only by MenuStructure.close() */
    fun allowClose(): Boolean
    /** Should implement unclose able menu logic */
    fun cancelClose(event: InventoryCloseEvent)
    /** Called to add new viewer to menu (or open it) */
    fun addViewer(player: Player)

    /** Redraw menu elements
     * @param special if specialized, will update only this element. if not, will update all menu elements.
     * */
    fun update(special: GuiElement? = null)

    /** Close menu for given viewer, if viewer is not given, close menu for all viewers. */
    fun close(closeFor: Player?)

    /** Update menu title */
    fun setTitle(title: Component)
}