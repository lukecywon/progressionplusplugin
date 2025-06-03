package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.SnowGlobe
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class SnowGlobeListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!SnowGlobe.isSnowGlobe(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true // Cancel snowball throw

        val originalVelocities = mutableMapOf<Int, Vector>()

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || ticks > 200) { cancel(); return }

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

                    val id = entity.entityId
                    if (!originalVelocities.containsKey(id)) {
                        originalVelocities[id] = entity.velocity.clone()
                    }

                    val baseVelocity = originalVelocities[id] ?: continue

                    // Boost if it's a projectile shot by the player
                    val multiplier = when {
                        entity is Projectile && entity.shooter == player -> 1.5
                        else -> 0.4
                    }

                    entity.velocity = baseVelocity.clone().multiply(multiplier)
                }

                ticks++
            }
        }.runTaskTimer(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 0L, 2L)
    }
}