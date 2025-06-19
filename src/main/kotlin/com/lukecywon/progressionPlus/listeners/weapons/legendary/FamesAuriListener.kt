package com.lukecywon.progressionPlus.listeners.weapons.legendary

import com.lukecywon.progressionPlus.items.weapons.legendary.FamesAuri
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot

class FamesAuriListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        val player = e.player
        val item = player.inventory.itemInMainHand
        if (!FamesAuri.isFamesAuri(item)) return

        FamesAuri.toggle(player)
    }

    @EventHandler
    fun onSwapHotbarSlot(e: PlayerItemHeldEvent) {
        val player = e.player
        FamesAuri.stopIfUnequipped(player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        FamesAuri.deactivate(e.player)
    }
}
