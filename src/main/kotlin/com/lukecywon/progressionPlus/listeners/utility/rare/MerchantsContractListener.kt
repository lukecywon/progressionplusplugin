package com.lukecywon.progressionPlus.listeners.utility.rare

import com.lukecywon.progressionPlus.gui.MerchantsGUI
import com.lukecywon.progressionPlus.gui.MerchantsTradeGUI
import com.lukecywon.progressionPlus.items.utility.rare.MerchantsContract
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerInteractEvent

class MerchantsContractListener : Listener {
    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!MerchantsContract.isThisItem(item)) return

        e.isCancelled = true
        MerchantsGUI.openPlayerSelectGUI(player)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        MerchantsGUI.handleClick(e)
        MerchantsTradeGUI.handleClick(e)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        if (MerchantsTradeGUI.getSession(player) != null) {
            MerchantsTradeGUI.handleClose(player)
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        val session = MerchantsTradeGUI.getSession(player) ?: return

        val isPlayer1 = player.uniqueId == session.player1.uniqueId
        val ownSlots = if (isPlayer1) MerchantsTradeGUI.player1Slots else MerchantsTradeGUI.player2Slots
        val validSlots = ownSlots.toSet()

        for (slot in event.rawSlots) {
            if (slot < event.view.topInventory.size && slot !in validSlots) {
                event.isCancelled = true
                return
            }
        }
    }
}
