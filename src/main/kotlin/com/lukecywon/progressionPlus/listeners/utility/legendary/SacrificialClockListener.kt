package com.lukecywon.progressionPlus.listeners.utility.legendary

import com.lukecywon.progressionPlus.gui.SacrificialClockGUI
import com.lukecywon.progressionPlus.items.utility.legendary.SacrificialClock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent

class SacrificialClockListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!SacrificialClock.isSacrificialClock(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true
        SacrificialClockGUI.open(player)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        SacrificialClockGUI.handleClick(e)
    }
}
