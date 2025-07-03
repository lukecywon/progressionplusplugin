package com.lukecywon.progressionPlus.listeners.utility.common

import com.lukecywon.progressionPlus.items.utility.common.ItemEncyclopedia
import com.lukecywon.progressionPlus.ui.ItemRecipeGUI
import com.lukecywon.progressionPlus.ui.ItemListGUI
import com.lukecywon.progressionPlus.ui.ItemObtainGUI
import com.lukecywon.progressionPlus.ui.RarityGUI
import com.lukecywon.progressionPlus.utils.GUIHistory
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent

class ItemEncyclopediaListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!ItemEncyclopedia.isThisItem(item)) return

        RarityGUI.open(player)
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)

        e.isCancelled = true
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        RarityGUI.handleClick(e)
        ItemListGUI.handleClick(e)
        ItemRecipeGUI.handleClick(e)
        ItemObtainGUI.handleClick(e)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        GUIHistory.clearStack(e.player.uniqueId)
    }
}
