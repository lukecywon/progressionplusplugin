package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.legendary.FamesAuri
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class WearableHeadListener : Listener {
    @EventHandler
    fun onHeadPlace(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.hand != EquipmentSlot.HAND) return

        val player = event.player
        val item = player.inventory.itemInMainHand
        if (!CustomItem.isCustomHead(item)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onHeadRightClick(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = event.player
        val item = player.inventory.itemInMainHand
        if (!CustomItem.isCustomHead(item)) return

        val helmet = player.inventory.helmet
        val headItem = item.clone().also { it.amount = 1 }
        val hand = event.hand ?: return

        // Put current helmet back into the hand slot
        player.inventory.setItem(hand, helmet)

        // Equip the head as helmet
        player.inventory.helmet = headItem

        player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f)

        event.isCancelled = true
    }
}