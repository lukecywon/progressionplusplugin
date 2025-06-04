package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.NocturnHood
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class NocturnHoodListener : Listener {

    private fun applyNightVision(player: Player) {
        val helmet = player.inventory.helmet ?: return
        if (NocturnHood.isNocturnHood(helmet)) {
            player.addPotionEffect(
                PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0, true, false, false)
            )
        }
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        applyNightVision(e.player)
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        applyNightVision(e.player)
    }
}
