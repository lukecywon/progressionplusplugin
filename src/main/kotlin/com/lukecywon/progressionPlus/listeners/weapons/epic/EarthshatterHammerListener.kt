package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.epic.EarthshatterHammer
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityChangeBlockEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.math.*

class EarthshatterHammerListener : Listener {
    private val plugin = ProgressionPlus.getPlugin()
    private val itemId = "earthshatter_hammer"
    private val cooldownMillis = 30_000L
    private val blockTag = NamespacedKey(plugin, "earthshatter")
    private val jumpLockKey = NamespacedKey(plugin, "gravitymaul_jumplock")

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!EarthshatterHammer.isThisItem(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cEarthshatter is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        e.isCancelled = true
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20, 5, false, false, false))
        player.world.playSound(player.location, Sound.BLOCK_END_GATEWAY_SPAWN, 1f, 0.5f)
        player.world.playSound(player.location, Sound.BLOCK_CONDUIT_ACTIVATE, 1f, 0.5f)
        player.world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_CHARGE, 3f, 0.4f)

        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (count++ >= 10) {
                    cancel()
                    return
                }

                val loc = player.location.clone().add(0.0, -0.5, 0.0)
                val material = loc.block.type.takeIf { it.isSolid } ?: Material.STONE
                val data = Bukkit.createBlockData(material)

                for (i in 0..6) {
                    val offsetX = (Math.random() - 0.5) * 1.5
                    val offsetZ = (Math.random() - 0.5) * 1.5
                    player.world.spawnParticle(
                        Particle.BLOCK_CRUMBLE,
                        loc.clone().add(offsetX, 1.1, offsetZ), // Moved Y +1
                        8,
                        0.1, 0.0, 0.1,
                        0.05,
                        data
                    )
                }
            }
        }.runTaskTimer(plugin, 0L, 2L)

        object : BukkitRunnable() {
            override fun run() {
                launchEarthshatter(player)
                CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
            }
        }.runTaskLater(plugin, 20L)
    }

    private fun launchEarthshatter(player: LivingEntity) {
        val origin = player.location.clone().add(0.0, -1.0, 0.0)
        val dir = player.location.direction.setY(0).normalize()
        val maxDistance = 12
        val arcHalfWidth = 12.0

        for (d in 1..maxDistance) {
            val forward = dir.clone().multiply(d.toDouble())
            val center = origin.clone().add(forward)

            val perp = Vector(-dir.z, 0.0, dir.x)
            val arcWidth = arcHalfWidth * (d.toDouble() / maxDistance)
            val steps = (arcWidth * 2).roundToInt()

            for (i in -steps / 2..steps / 2) {
                val horizOffset = perp.clone().multiply(i.toDouble() * 0.6)
                val baseLoc = center.clone().add(horizOffset)

                for (dy in -2..2) {
                    val loc = baseLoc.clone().add(0.0, dy.toDouble(), 0.0)
                    val block = loc.block
                    if (block.type == Material.AIR || !block.type.isSolid) continue

                    val blockData = block.blockData
                    val delay = d * 2L
                    val hasAffected = mutableSetOf<UUID>()

                    object : BukkitRunnable() {
                        override fun run() {
                            val spawnLoc = block.location.clone().add(0.0, 0.1, 0.0)
                            val fake = block.world.spawnFallingBlock(spawnLoc, blockData)
                            fake.velocity = Vector(0.0, 0.7, 0.0)
                            fake.dropItem = false
                            fake.setHurtEntities(false)
                            fake.persistentDataContainer.set(blockTag, PersistentDataType.BYTE, 1)

                            block.world.spawnParticle(
                                Particle.BLOCK_CRUMBLE,
                                block.location.clone().add(0.0, 1.0, 0.0), // Y +1 for effect
                                15,
                                0.3, 0.3, 0.3,
                                0.1,
                                blockData
                            )

                            val nearby = block.location.getNearbyLivingEntities(1.5)
                            for (entity in nearby) {
                                if (entity.uniqueId == player.uniqueId || hasAffected.contains(entity.uniqueId)) continue
                                hasAffected.add(entity.uniqueId)

                                entity.damage(12.5, player)
                                entity.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 100, 5, false, false, true))

                                val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)
                                if (jumpAttr != null) {
                                    val data = entity.persistentDataContainer
                                    if (!data.has(jumpLockKey, PersistentDataType.DOUBLE)) {
                                        data.set(jumpLockKey, PersistentDataType.DOUBLE, jumpAttr.baseValue)
                                        jumpAttr.baseValue = 0.0
                                    }
                                }

                                val knockVec = entity.location.toVector().subtract(player.location.toVector())
                                    .normalize().multiply(0.4).setY(1.2)
                                entity.velocity = knockVec
                            }
                        }
                    }.runTaskLater(plugin, delay)
                }
            }
        }

        player.world.playSound(player.location, Sound.ENTITY_WITHER_BREAK_BLOCK, 1f, 0.6f)
        player.world.spawnParticle(Particle.EXPLOSION, player.location, 1)
    }

    @EventHandler
    fun onBlockLand(event: EntityChangeBlockEvent) {
        val falling = event.entity as? FallingBlock ?: return
        val data = falling.persistentDataContainer
        if (data.has(blockTag, PersistentDataType.BYTE)) {
            event.isCancelled = true
        }
    }

    private fun Location.getNearbyLivingEntities(radius: Double): List<LivingEntity> {
        return this.world.getNearbyEntities(this, radius, radius, radius)
            .filterIsInstance<LivingEntity>()
    }
}
