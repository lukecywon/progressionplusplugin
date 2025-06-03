package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.Snowglobe
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class SnowglobeListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!Snowglobe.isSnowglobe(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true // Cancel snowball throw

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || ticks > 200) { cancel(); return }

                val center = player.location
                val radius = 5.0

                // Show particle ring
                for (angle in 0 until 360 step 10) {
                    val rad = Math.toRadians(angle.toDouble())
                    val x = radius * Math.cos(rad)
                    val z = radius * Math.sin(rad)
                    val loc = center.clone().add(x, 0.0, z)
                    player.world.spawnParticle(Particle.SNOWFLAKE, loc, 1, 0.0, 0.1, 0.0, 0.01)
                }

                // Slow entities/projectiles inside
                val nearby = player.world.getNearbyEntities(center, radius, radius, radius)
                for (entity in nearby) {
                    if (entity == player) continue
                    if (entity is LivingEntity || entity is Projectile) {
                        val velocity = entity.velocity
                        entity.velocity = Vector(velocity.x * 0.5, velocity.y * 0.5, velocity.z * 0.5)
                    }
                }

                ticks++
            }
        }.runTaskTimer(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 0L, 2L) // every 2 ticks (0.1s)
    }
}