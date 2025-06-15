package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.listeners.mechanics.LegendaryItemListener
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object LegendaryManager : Manager {
    override fun start(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            var counter = 0
            val killValue = 10

            override fun run() {
                for (player in plugin.server.onlinePlayers) {
                    if (counter >= killValue) {
                        punishPlayer(player)
                        counter = 0
                    }

                    if (LegendaryItemListener.hasMoreThanOneLegendary(player)) {
                        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20, 255))
                        player.addPotionEffect(PotionEffect(PotionEffectType.JUMP_BOOST, 20, 128))
                        player.showTitle(
                            Title.title(
                                Component.text("WARNING!", NamedTextColor.RED),
                                Component.text("You are overflowing with LEGENDARY power." + " (" + (killValue - counter) + "s)", NamedTextColor.GOLD)
                            )
                        )

                        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.8f)
                        counter++
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    fun punishPlayer(player: Player) {
        // Spawn angry particles around the player
        val loc = player.location.clone().add(0.0, 1.0, 0.0)  // Particle center
        val world = player.world
        world.spawnParticle(Particle.ASH, loc, 30, 0.5, 1.0, 0.5, 0.01)

        // Play warning sound
        world.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 0.5f)

        player.damage(999.0)
        world.spawnParticle(Particle.EXPLOSION, player.location, 1)
        world.playSound(player.location, Sound.ENTITY_GENERIC_EXPLODE, 2f, 0.8f)
    }

}