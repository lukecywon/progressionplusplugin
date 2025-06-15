package com.lukecywon.progressionPlus.listeners.armor.rare

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.armor.uncommon.NocturnHood
import org.bukkit.Bukkit
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
                    val helmet = player.inventory.helmet

                    if (helmet != null && NocturnHood.isNocturnHood(helmet)) {
                        active.add(player.uniqueId)

                        val effect = player.getPotionEffect(PotionEffectType.NIGHT_VISION)
                        val duration = effect?.duration ?: 0

                        // Reapply only if not present or less than 100 ticks left
                        if (effect == null || duration < 240) {
                            player.addPotionEffect(
                                PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0, false, false, false)
                            )
                        }
                    } else {
                        if (active.contains(player.uniqueId)) {
                            player.removePotionEffect(PotionEffectType.NIGHT_VISION)
                            active.remove(player.uniqueId)
                        }
                    }
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 20L) // every second
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
