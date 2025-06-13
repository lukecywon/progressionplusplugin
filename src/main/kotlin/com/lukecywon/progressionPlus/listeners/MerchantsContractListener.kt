package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.gui.MerchantsGUI
import com.lukecywon.progressionPlus.items.MerchantsContract
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent

class MerchantsContractListener : Listener {
    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!MerchantsContract.isMerchantsContract(item)) return

        e.isCancelled = true
        MerchantsGUI.openPlayerSelectGUI(player)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        MerchantsGUI.handleClick(e)
    }
}
