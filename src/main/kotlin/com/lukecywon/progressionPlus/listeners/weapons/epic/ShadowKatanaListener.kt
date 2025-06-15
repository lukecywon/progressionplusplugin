package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.epic.ShadowKatana
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ShadowKatanaListener : Listener {
    private val itemId = "shadow_katana"
    private val cooldownMillis = 15_000L
    private val activeTrails = mutableMapOf<UUID, List<Location>>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!ShadowKatana.isShadowKatana(item)) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cShadow Katana is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)

        val start = player.location.clone()
        val direction = player.location.direction.normalize()
        val dashDistance = 8
        val trail = mutableListOf<Location>()

        for (i in 0..dashDistance) {
            val point = start.clone().add(direction.clone().multiply(i.toDouble()))
            trail.add(point)

            point.getNearbyLivingEntities(1.0).forEach { entity ->
                if (entity.uniqueId != player.uniqueId &&
                    entity !is ArmorStand &&
                    (entity !is Tameable || entity.owner?.uniqueId != player.uniqueId)
                ) {
                    entity.damage(7.0, player)
                    entity.world.spawnParticle(Particle.SWEEP_ATTACK, entity.location, 5, 0.3, 0.3, 0.3, 0.05)
                }
            }
        }

        player.setCollidable(false)
        player.velocity = direction.multiply(2.2)
        player.world.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f)
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            player.setCollidable(true)
        }, 10L)

        val uuid = player.uniqueId
        activeTrails[uuid] = trail.toList()

        object : BukkitRunnable() {
            var ticksElapsed = 0
            override fun run() {
                val trail = activeTrails[uuid] ?: run {
                    cancel()
                    return
                }

                val world = player.world
                val hitThisWave = mutableSetOf<UUID>()

                repeat((3..5).random()) {
                    val loc = trail.random()
                    loc.world.spawnParticle(Particle.SWEEP_ATTACK, loc, 5, 0.5, 0.2, 0.5, 0.05)
                    loc.world.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5f, 1.2f)

                    loc.getNearbyLivingEntities(1.8).forEach { entity ->
                        if (entity.uniqueId == player.uniqueId) return@forEach
                        if (hitThisWave.contains(entity.uniqueId)) return@forEach
                        if (entity is Tameable && entity.owner?.uniqueId == player.uniqueId) return@forEach
                        if (entity is ArmorStand) return@forEach

                        entity.damage(5.0, player)
                        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 60, 0)) // Slowness I
                        entity.world.playSound(entity.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 0.8f)
                        hitThisWave.add(entity.uniqueId)
                    }
                }


                ticksElapsed += 4
                if (ticksElapsed >= 100) {
                    activeTrails.remove(uuid)
                    cancel()
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 5L)

        e.isCancelled = true
    }

    private fun Location.getNearbyLivingEntities(radius: Double): List<LivingEntity> {
        return this.world?.getNearbyEntities(this, radius, radius, radius)
            ?.filterIsInstance<LivingEntity>() ?: emptyList()
    }
}
