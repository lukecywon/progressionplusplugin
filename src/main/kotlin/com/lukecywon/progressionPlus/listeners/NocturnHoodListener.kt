package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.NocturnHood
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class NocturnHoodListener : Listener {

    private val active = mutableSetOf<UUID>()

    init {
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    val helmet = player.inventory.helmet ?: continue
                    if (!NocturnHood.isNocturnHood(helmet)) {
                        active.remove(player.uniqueId)
                        continue
                    }

                    active.add(player.uniqueId)

                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.NIGHT_VISION, 40, 0, false, false, false)
                    )
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 20L) // Runs every second
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        active.add(e.player.uniqueId)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        active.remove(e.player.uniqueId)
    }
}
