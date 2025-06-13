package com.lukecywon.progressionPlus.gui

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent

abstract class GUI(private val title: String, private val size: Int) {
    fun getTitleComponent(): Component {
        return Component.text(title)
    }

    fun getTitle(): String {
        return title
    }

    fun getSize(): Int {
        return size
    }

    abstract fun open(player: Player)
    abstract fun handleClick(e: InventoryClickEvent)
}