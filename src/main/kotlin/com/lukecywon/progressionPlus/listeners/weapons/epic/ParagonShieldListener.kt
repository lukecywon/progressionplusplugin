package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.epic.ParagonShield
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class ParagonShieldListener : Listener {

    private val config = ParagonShield.CONFIG
    private val activeParries = mutableSetOf<UUID>()
    private val pendingActivations = mutableSetOf<UUID>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = when (e.hand) {
            EquipmentSlot.HAND -> player.inventory.itemInMainHand
            EquipmentSlot.OFF_HAND -> player.inventory.itemInOffHand
            else -> return
        }
        if (!ParagonShield.isParagonShield(item)) return
        if (!e.action.name.contains("RIGHT_CLICK")) return

        val uuid = player.uniqueId
        if (CustomItem.isOnCooldown("paragon_shield", uuid)) {
            val remaining = CustomItem.getCooldownRemaining("paragon_shield", uuid) / 1000.0
            player.sendActionBar(
                Component.text("⏳ Paragon Shield on cooldown: %.1f seconds left".format(remaining))
                    .color(NamedTextColor.RED)
            )
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f)
            return
        }

        // Start cooldown
        CustomItem.setCooldown("paragon_shield", uuid, config.cooldownMillis)
        pendingActivations.add(uuid)

        // Visual + resistance
        ParagonShield.setActiveModel(item)
        player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, config.parryWindowTicks.toInt(), config.resistanceLevel, false, false))
        player.playSound(player.location, Sound.BLOCK_BEACON_AMBIENT, 1f, 1.2f)

        // Revert model after 1 second
        object : BukkitRunnable() {
            override fun run() {
                if (ParagonShield.isParagonShield(item)) {
                    ParagonShield.resetModel(item)
                }
                pendingActivations.remove(uuid)
                activeParries.remove(uuid)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), config.parryWindowTicks)

        // Light blue particles during parry window
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (!pendingActivations.contains(uuid)) {
                    cancel()
                    return
                }
                player.world.spawnParticle(
                    Particle.INSTANT_EFFECT,
                    player.location.add(0.0, 1.0, 0.0),
                    8, // reduced from 20
                    0.13, 0.1, 0.13,
                    0.01
                )
                count++
                if (count >= config.parryWindowTicks) cancel()
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 1L)
    }

    @EventHandler
    fun onDamaged(e: EntityDamageByEntityEvent) {
        val player = e.entity as? Player ?: return
        val uuid = player.uniqueId
        if (!pendingActivations.contains(uuid)) return

        // ✅ Successful parry trigger
        e.isCancelled = true
        e.damage = 0.0
        pendingActivations.remove(uuid)
        activeParries.add(uuid)

        object : BukkitRunnable() {
            override fun run() {
                performParry(player)
                activeParries.remove(uuid)
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
}
