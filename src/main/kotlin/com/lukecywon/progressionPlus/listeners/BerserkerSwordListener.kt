package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.mechanics.BerserkerSwordManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot

class BerserkerSwordListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        BerserkerSwordManager.handleRightClick(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        BerserkerSwordManager.cleanup(e.player)
    }

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        BerserkerSwordManager.restoreHealthOnDeath(e.entity)
    }
}