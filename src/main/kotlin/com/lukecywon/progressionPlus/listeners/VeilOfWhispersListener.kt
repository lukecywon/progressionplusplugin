package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.VeilOfWhispers
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class VeilOfWhispersListener : Listener {
    private val cooldowns = mutableMapOf<UUID, Long>()
    private val activePhasewalk = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!VeilOfWhispers.isVeilOfWhispers(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        val now = System.currentTimeMillis()
        val lastUsed = cooldowns[player.uniqueId] ?: 0L
        if (now - lastUsed < 15_000) {
            val secondsLeft = ((15_000 - (now - lastUsed)) / 1000).toInt()
            player.sendMessage("§cPhasewalk is on cooldown for $secondsLeft more seconds.")
            return
        }

        cooldowns[player.uniqueId] = now
        activePhasewalk[player.uniqueId] = now + 10_000

        player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 200, 1, false, false))
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1, false, false))
        player.sendMessage("§5You phase into the void. 10 seconds remaining.")

        object : BukkitRunnable() {
            var secondsLeft = 10
            override fun run() {
                if (!player.isOnline || player.uniqueId !in activePhasewalk) {
                    cancel(); return
                }
                secondsLeft--
                player.sendActionBar("§7Phasewalk: §f$secondsLeft§7s remaining")
                if (secondsLeft <= 0) {
                    endPhasewalk(player)
                    cancel(); return
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 20L, 20L)
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val player = e.player
        if (player.uniqueId !in activePhasewalk) return

        val deltaY = e.to.y - e.from.y
        val loc = player.location.clone()
        when {
            player.isSneaking -> loc.subtract(0.0, 0.3, 0.0)
            deltaY > 0.3 -> loc.add(0.0, 0.3, 0.0)
        }
        loc.world.spawnParticle(Particle.SOUL, loc, 2, 0.2, 0.1, 0.2, 0.01)
    }

    private fun endPhasewalk(player: Player) {
        activePhasewalk.remove(player.uniqueId)
        player.removePotionEffect(PotionEffectType.INVISIBILITY)
        player.removePotionEffect(PotionEffectType.SLOW_FALLING)
        player.sendMessage("§7You return from the veil.")
    }
}