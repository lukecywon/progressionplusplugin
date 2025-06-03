package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.gui.WormholeGUI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class WormholeGUIListener : Listener {
    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        WormholeGUI.handleClick(e)
    }
}
