package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.utility.legendary.SnowGlobe
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
    private val itemId = "snow_globe"
    private val cooldownMillis = 15_000L

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand


        if (!SnowGlobe.isSnowGlobe(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true // Always cancel snowball throw

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cSnowglobe is on cooldown for ${minutes}m ${seconds}s.")
            return
        }

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)

        val originalVelocities = mutableMapOf<Int, Vector>()

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || ticks > 50) { cancel(); return }

                val center = player.location
                val radius = 7.0

                // Particle ring
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

                    val original = originalVelocities[id] ?: continue
                    val scaled = when {
                        entity is Projectile -> original.clone().multiply(0.25)
                        entity is LivingEntity -> original.clone().multiply(0.6)
                        else -> continue
                    }

                    entity.velocity = scaled
                }

                ticks++
            }
        }.runTaskTimer(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 0L, 2L)
    }
}
