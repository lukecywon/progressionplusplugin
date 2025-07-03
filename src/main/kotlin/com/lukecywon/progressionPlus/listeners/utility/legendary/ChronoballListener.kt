package com.lukecywon.progressionPlus.listeners.utility.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.utility.legendary.Chronoball
import com.lukecywon.progressionPlus.items.utility.legendary.Chronoball.CONFIG
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class ChronoballListener : Listener {
    private val itemId = "chronoball"
    private val activeChronoballUsers = mutableSetOf<UUID>()
    private val damageTakenMap = mutableMapOf<UUID, Double>()
    private val affectedEntities = mutableMapOf<UUID, AffectedStats>()

    data class AffectedStats(
        val originalSpeed: Double,
        val speedModifier: AttributeModifier,
        val originalJump: Double?
    )

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        val item = e.itemDrop.itemStack
        if (Chronoball.isThisItem(item)) {
            Chronoball.resetModel(item)
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        val item = e.player.inventory.itemInMainHand
        if (Chronoball.isThisItem(item)) {
            Chronoball.resetModel(item)
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = e.item ?: return
        if (!Chronoball.isThisItem(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true
        val uuid = player.uniqueId

        // ✅ Recast for healing
        if (uuid in activeChronoballUsers) {
            val totalDamage = damageTakenMap[uuid] ?: 0.0
            if (totalDamage > 0.0) {
                val healAmount = totalDamage * CONFIG.healPercentage
                player.health = (player.health + healAmount).coerceAtMost(player.maxHealth)

                player.world.spawnParticle(Particle.HEART, player.location.add(0.0, 1.0, 0.0), 10, 0.4, 0.6, 0.4)
                player.world.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1.2f)
            }

            // Finish effect early after recast
            Chronoball.resetModel(item)
            activeChronoballUsers.remove(uuid)
            damageTakenMap.remove(uuid)
            return
        }

        // ❌ Cooldown
        if (CustomItem.isOnCooldown(itemId, uuid)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, uuid)
            val seconds = (millisLeft / 1000) % 60
            val minutes = (millisLeft / 1000) / 60
            player.sendMessage("§cSnowglobe is on cooldown for ${minutes}m ${seconds}s.")
            return
        }

        CustomItem.setCooldown(itemId, uuid, CONFIG.cooldownTicks * 50)
        activeChronoballUsers.add(uuid)
        damageTakenMap[uuid] = 0.0
        Chronoball.setActiveModel(item)

        val projectileVelocities = mutableMapOf<Int, Vector>()

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!player.isOnline || uuid !in activeChronoballUsers) {
                    restoreAll()
                    cancel()
                    return
                }

                val center = player.location
                val radius = CONFIG.radius

                // ❄️ Draw circle
                for (angle in 0 until 360 step 10) {
                    val rad = Math.toRadians(angle.toDouble())
                    val x = radius * Math.cos(rad)
                    val z = radius * Math.sin(rad)
                    val loc = center.clone().add(x, 0.0, z)
                    player.world.spawnParticle(Particle.DRAGON_BREATH, loc, 4, 0.2, 0.2, 0.2, 0.01)
                    player.world.spawnParticle(Particle.END_ROD, loc, 2, 0.1, 0.1, 0.1, 0.0)
                }

                val nearby = player.world.getNearbyEntities(center, radius, radius, radius)
                val activeEntityUUIDs = mutableSetOf<UUID>()

                for (entity in nearby) {
                    if (entity == player) continue

                    if (entity is Projectile && entity.entityId !in projectileVelocities) {
                        entity.velocity = entity.velocity.multiply(CONFIG.projectileVelocityMultiplier)
                        projectileVelocities[entity.entityId] = entity.velocity
                        continue
                    }

                    if (entity is LivingEntity) {
                        val entUUID = entity.uniqueId
                        activeEntityUUIDs.add(entUUID)

                        val speedAttr = entity.getAttribute(Attribute.MOVEMENT_SPEED) ?: continue
                        val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)

                        if (!affectedEntities.containsKey(entUUID)) {
                            val originalSpeed = speedAttr.baseValue
                            val originalJump = jumpAttr?.baseValue
                            val speedModifier = AttributeModifier(
                                UUID.randomUUID(),
                                "snowglobe_slow",
                                CONFIG.movementSlow * -originalSpeed,
                                AttributeModifier.Operation.ADD_NUMBER
                            )
                            speedAttr.addModifier(speedModifier)
                            if (jumpAttr != null) jumpAttr.baseValue = 0.0

                            affectedEntities[entUUID] = AffectedStats(originalSpeed, speedModifier, originalJump)
                        }
                    }
                }

                val toRemove = affectedEntities.keys.filter { it !in activeEntityUUIDs }
                for (entUUID in toRemove) {
                    val entity = Bukkit.getEntity(entUUID) as? LivingEntity ?: continue
                    val stats = affectedEntities.remove(entUUID) ?: continue
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
                if (ticks >= CONFIG.durationTicks / 2) {
                    restoreAll()
                    cancel()
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 2L)

        // ⏳ End heal window after full duration
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            Chronoball.resetModel(item)
            activeChronoballUsers.remove(uuid)
            damageTakenMap.remove(uuid)
        }, CONFIG.durationTicks)
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

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        val uuid = player.uniqueId
        if (uuid !in activeChronoballUsers) return
        damageTakenMap[uuid] = (damageTakenMap[uuid] ?: 0.0) + e.finalDamage
    }
}
