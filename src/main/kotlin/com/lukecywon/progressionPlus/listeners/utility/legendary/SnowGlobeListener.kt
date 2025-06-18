package com.lukecywon.progressionPlus.listeners.utility.legendary

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.utility.legendary.SnowGlobe
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class SnowGlobeListener : Listener {
    private val itemId = "snow_globe"
    private val cooldownMillis = 15_000L

    private data class AffectedStats(
        val originalSpeed: Double,
        val speedModifier: AttributeModifier,
        val originalJump: Double?
    )

    private val affectedEntities = mutableMapOf<UUID, AffectedStats>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!SnowGlobe.isSnowGlobe(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cSnowglobe is on cooldown for ${minutes}m ${seconds}s.")
            return
        }

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)

        val projectileVelocities = mutableMapOf<Int, Vector>()

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || ticks > 50) {
                    restoreAll()
                    cancel()
                    return
                }

                val center = player.location
                val radius = 7.0

                for (angle in 0 until 360 step 10) {
                    val rad = Math.toRadians(angle.toDouble())
                    val x = radius * Math.cos(rad)
                    val z = radius * Math.sin(rad)
                    val loc = center.clone().add(x, 0.0, z)
                    player.world.spawnParticle(Particle.SNOWFLAKE, loc, 1, 0.0, 0.1, 0.0, 0.01)
                }

                val nearby = player.world.getNearbyEntities(center, radius, radius, radius)
                val activeEntityUUIDs = mutableSetOf<UUID>()

                for (entity in nearby) {
                    if (entity == player) continue

                    if (entity is Projectile && entity.entityId !in projectileVelocities) {
                        entity.velocity = entity.velocity.multiply(0.1)
                        projectileVelocities[entity.entityId] = entity.velocity
                        continue
                    }

                    if (entity is LivingEntity) {
                        val uuid = entity.uniqueId
                        activeEntityUUIDs.add(uuid)

                        val speedAttr = entity.getAttribute(Attribute.MOVEMENT_SPEED) ?: continue
                        val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)

                        if (!affectedEntities.containsKey(uuid)) {
                            val originalSpeed = speedAttr.baseValue
                            val originalJump = jumpAttr?.baseValue
                            val speedModifier = AttributeModifier(UUID.randomUUID(), "snowglobe_slow", -0.5 * originalSpeed, AttributeModifier.Operation.ADD_NUMBER)
                            speedAttr.addModifier(speedModifier)
                            if (jumpAttr != null) {
                                jumpAttr.baseValue = 0.0
                            }
                            affectedEntities[uuid] = AffectedStats(originalSpeed, speedModifier, originalJump)
                        }
                    }
                }

                val toRemove = affectedEntities.keys.filter { it !in activeEntityUUIDs }
                for (uuid in toRemove) {
                    val entity = Bukkit.getEntity(uuid) as? LivingEntity ?: continue
                    val stats = affectedEntities.remove(uuid) ?: continue
                    val speedAttr = entity.getAttribute(Attribute.MOVEMENT_SPEED) ?: continue
                    speedAttr.removeModifier(stats.speedModifier)
                    if (speedAttr.modifiers.none { it.name == "snowglobe_slow" }) {
                        speedAttr.baseValue = stats.originalSpeed
                    }
                    val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)
                    if (jumpAttr != null && stats.originalJump != null) {
                        jumpAttr.baseValue = stats.originalJump
                    }
                }

                ticks++
            }
        }.runTaskTimer(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 0L, 2L)
    }

    private fun restoreAll() {
        for ((uuid, stats) in affectedEntities) {
            val entity = Bukkit.getEntity(uuid) as? LivingEntity ?: continue
            val speedAttr = entity.getAttribute(Attribute.MOVEMENT_SPEED) ?: continue
            speedAttr.removeModifier(stats.speedModifier)
            if (speedAttr.modifiers.none { it.name == "snowglobe_slow" }) {
                speedAttr.baseValue = stats.originalSpeed
            }
            val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)
            if (jumpAttr != null && stats.originalJump != null) {
                jumpAttr.baseValue = stats.originalJump
            }
        }
        affectedEntities.clear()
    }
}
