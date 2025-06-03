package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.WormholePotion
import com.lukecywon.progressionPlus.gui.WormholeGUI
import com.lukecywon.progressionPlus.mechanics.TeleportRequestManager
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.scheduler.BukkitRunnable

class WormholePotionListener : Listener {
    @EventHandler
    fun onDrinkWormholePotion(e: PlayerItemConsumeEvent) {
        val player = e.player

        if (!WormholePotion.isWormholePotion(e.item)) return

        if (TeleportRequestManager.isOnCooldown(player.uniqueId)) {
            val secondsLeft = TeleportRequestManager.secondsLeft(player.uniqueId)
            player.sendMessage("§cYou must wait §e$secondsLeft§c seconds before using another Wormhole Potion.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.8f, 0.6f)

            // Refund potion
            player.inventory.addItem(e.item.clone())
            return
        }

        val potionClone = e.item.clone()

        object : BukkitRunnable() {
            override fun run() {
                val opened = WormholeGUI.open(player, potionClone)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 1L)
    }
}
