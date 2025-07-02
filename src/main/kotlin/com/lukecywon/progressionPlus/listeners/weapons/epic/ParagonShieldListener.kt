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
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class ParagonShieldListener : Listener {

    private val cooldowns = mutableMapOf<UUID, Long>()
    private val config = ParagonShield.CONFIG

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val hand = event.hand ?: return
        val item = if (hand == EquipmentSlot.HAND) {
            player.inventory.itemInMainHand
        } else {
            player.inventory.itemInOffHand
        }

        if (!ParagonShield.isParagonShield(item)) return
        if (!event.action.name.contains("RIGHT_CLICK")) return

        val uuid = player.uniqueId
        val now = System.currentTimeMillis()
        if (cooldowns[uuid]?.let { it > now } == true) return

        // Apply cooldown
        cooldowns[uuid] = now + config.cooldownTicks * 50

        // Switch model temporarily
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield_1")
        item.itemMeta = meta
        updateHeldItem(player, hand, item)

        // Give Resistance
        player.addPotionEffect(
            PotionEffect(
                PotionEffectType.RESISTANCE,
                config.resistanceDurationTicks.toInt(),
                config.resistanceAmplifier,
                false, false, true
            )
        )

        // Reset model after resistance duration
        object : BukkitRunnable() {
            override fun run() {
                ParagonShield.resetModel(item)
                updateHeldItem(player, hand, item)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), config.resistanceDurationTicks)

        // Trigger parry effect after windup
        object : BukkitRunnable() {
            override fun run() {
                performParry(player)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), config.windupTicks)
    }

    private fun updateHeldItem(player: Player, hand: EquipmentSlot, item: ItemStack) {
        when (hand) {
            EquipmentSlot.HAND -> player.inventory.setItemInMainHand(item)
            EquipmentSlot.OFF_HAND -> player.inventory.setItem(EquipmentSlot.OFF_HAND, item)
            else -> {}
        }
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
}
