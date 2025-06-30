package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.epic.ParagonShield
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ParagonShieldListener : Listener {

    private val blockTimestamps = mutableMapOf<UUID, Long>()
    private val cooldowns = mutableMapOf<UUID, Long>()
    private val config = ParagonShield.CONFIG

    @EventHandler
    fun onBlock(event: EntityDamageByEntityEvent) {
        val player = event.entity as? Player ?: return
        val shield = player.inventory.itemInOffHand
        if (!ParagonShield.isThisItem(shield)) return
        if (!player.isBlocking) return

        val uuid = player.uniqueId
        val now = System.currentTimeMillis()

        if (cooldowns[uuid]?.let { it > now } == true) {
            player.playSound(player.location, Sound.ITEM_SHIELD_BREAK, 1f, 0.6f)
            return
        }

        val blockStart = blockTimestamps.getOrPut(uuid) { now }
        val timeHeld = now - blockStart

        if (timeHeld > config.parryWindowTicks * 50) {
            // ❌ Held too long
            cooldowns[uuid] = now + config.cooldownTicks * 50
            blockTimestamps.remove(uuid)
            player.playSound(player.location, Sound.ITEM_SHIELD_BREAK, 1f, 0.8f)
            return
        }

        // ✅ Successful parry
        event.damage = 0.0
        event.isCancelled = true
        cooldowns[uuid] = now + config.cooldownTicks * 50
        blockTimestamps.remove(uuid)
        player.playSound(player.location, Sound.ITEM_SHIELD_BLOCK, 1f, 1.2f)

        object : BukkitRunnable() {
            override fun run() {
                performParry(player)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), config.windupTicks)
    }

    private fun performParry(player: Player) {
        val world = player.world
        val origin = player.location.clone().add(0.0, 1.0, 0.0)
        val forward = origin.direction.normalize()
        val right = forward.clone().crossProduct(Vector(0, 1, 0)).normalize()
        val up = Vector(0, 1, 0)

        val center = origin.toVector()
        val hitEntities = mutableSetOf<LivingEntity>()

        for (x in -config.aoeWidth / 2..config.aoeWidth / 2) {
            for (y in 0 until config.aoeHeight) {
                for (z in 1..config.aoeLength) {
                    val offset = right.clone().multiply(x.toDouble())
                        .add(up.clone().multiply(y.toDouble()))
                        .add(forward.clone().multiply(z.toDouble()))

                    val pos = center.clone().add(offset)
                    world.spawnParticle(Particle.CRIT, pos.toLocation(world), 1, 0.0, 0.0, 0.0, 0.0)

                    world.getNearbyEntities(pos.toLocation(world), 0.5, 0.5, 0.5).forEach { entity ->
                        if (entity is LivingEntity && entity != player) {
                            hitEntities.add(entity)
                        }
                    }
                }
            }
        }

        for (target in hitEntities) {
            target.damage(config.damage, player)

            val knockVec = target.location.toVector().subtract(player.location.toVector()).normalize()
            target.velocity = target.velocity.add(knockVec.multiply(config.knockbackStrength)).setY(0.25)

            val attr = target.getAttribute(Attribute.ARMOR) ?: continue
            if (attr.value <= 0.0) continue

            val shredId = UUID.randomUUID()
            val shredMod = AttributeModifier(
                shredId,
                "paragon_shield_shred_$shredId",
                config.armorShred,
                AttributeModifier.Operation.ADD_NUMBER
            )
            attr.addModifier(shredMod)

            object : BukkitRunnable() {
                override fun run() {
                    attr.removeModifier(shredMod)
                }
            }.runTaskLater(ProgressionPlus.getPlugin(), config.shredDurationTicks)
        }

        world.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f)
    }


    init {
        // Clean up stale block holds
        Bukkit.getScheduler().runTaskTimer(ProgressionPlus.getPlugin(), Runnable {
            val now = System.currentTimeMillis()
            blockTimestamps.entries.removeIf { now - it.value > config.parryWindowTicks * 50 }
        }, 20L, 20L)
    }
}
