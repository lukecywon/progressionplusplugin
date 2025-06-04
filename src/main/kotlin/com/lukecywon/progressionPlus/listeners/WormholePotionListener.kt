package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.WormholePotion
import com.lukecywon.progressionPlus.gui.WormholeGUI
import com.lukecywon.progressionPlus.mechanics.TeleportRequestManager
import org.bukkit.Material
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
            val totalSeconds = TeleportRequestManager.secondsLeft(player.uniqueId)
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            player.sendMessage("§cYou must wait §e${minutes}m ${seconds}s§c before using another Wormhole Potion.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.8f, 0.6f)

            // Refund potion
            val bottle = player.inventory.contents.firstOrNull { it?.type == Material.GLASS_BOTTLE }
            bottle?.let {
                it.amount -= 1
                if (it.amount <= 0) player.inventory.remove(it)
            }
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
