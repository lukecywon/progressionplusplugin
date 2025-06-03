package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.SnowGlobe
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class SnowGlobeListener : Listener {
    private val cooldowns = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!SnowGlobe.isSnowGlobe(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        val now = System.currentTimeMillis()
        val lastUsed = cooldowns[player.uniqueId] ?: 0L
        if (now - lastUsed < 15_000) {
            val secondsLeft = ((15_000 - (now - lastUsed)) / 1000).toInt()
            player.sendMessage("§cSnow Globe is on cooldown for $secondsLeft more seconds.")
            return
        }

        e.isCancelled = true // Cancel snowball throw
        cooldowns[player.uniqueId] = now

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || ticks > 100) { cancel(); return }

                val center = player.location
                val radius = 6.0

                // Show particle ring
                for (angle in 0 until 360 step 10) {
                    val rad = Math.toRadians(angle.toDouble())
                    val x = radius * Math.cos(rad)
                    val z = radius * Math.sin(rad)
                    val loc = center.clone().add(x, 0.0, z)
                    player.world.spawnParticle(Particle.SNOWFLAKE, loc, 1, 0.0, 0.1, 0.0, 0.01)
                }

                val nearby = player.world.getNearbyEntities(center, radius, radius, radius)
                for (entity in nearby) {
                    if (entity == player) continue
                    val velocity = entity.velocity
                    entity.velocity = when (entity) {
                        is Projectile -> velocity.multiply(0.25)
                        is LivingEntity -> velocity.multiply(0.6)
                        else -> velocity
                    }
                }

                ticks++
            }
        }.runTaskTimer(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 0L, 2L)
    }
}