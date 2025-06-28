package com.lukecywon.progressionPlus.listeners.weapons.rare

import com.lukecywon.progressionPlus.items.weapons.rare.AshbornePendant
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import kotlin.math.pow

class AshbornePendantListener : Listener {
    private val CONE_ID = "ashborne_cone"
    private val CONE_COOLDOWN = 15_000L

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        if (event.hand != EquipmentSlot.HAND) return
        if (!AshbornePendant.isThisItem(player.inventory.itemInMainHand)) return

        if (CustomItem.isOnCooldown(CONE_ID, player.uniqueId)) {
            val ms = CustomItem.getCooldownRemaining(CONE_ID, player.uniqueId)
            val sec = ms / 1000
            player.sendMessage("§cFlame Cone is on cooldown: §e${sec}s")
            return
        }

        CustomItem.setCooldown(CONE_ID, player.uniqueId, CONE_COOLDOWN)

        val origin = player.location.add(0.0, 1.0, 0.0)
        val direction = player.location.direction.normalize()
        val range = 7.0
        val coneAngle = Math.toRadians(80.0)

        val world = player.world
        val steps = 40
        for (r in 1..7) {
            for (i in 0 until steps) {
                val angle = coneAngle * (i.toDouble() / steps - 0.5)
                val rotated = direction.clone().rotateAroundY(angle)
                val point = origin.clone().add(rotated.multiply(r.toDouble()))
                world.spawnParticle(Particle.FLAME, point, 0, 0.0, 0.0, 0.0, 0.01)
            }
        }

        val nearby = player.getNearbyEntities(range, range, range)
        for (entity in nearby) {
            if (entity is LivingEntity && entity != player) {
                val toTarget = entity.location.toVector().subtract(player.location.toVector()).normalize()
                val angle = direction.angle(toTarget)
                if (angle <= coneAngle / 2) {
                    val distance = player.location.distance(entity.location)
                    val knockScale = ((range - distance) / range).coerceIn(0.1, 1.0)

                    // Adjusted scaling: up close gets more push, far gets softer
                    val knockStrength = 1.0 * knockScale.pow(1.2) // nonlinear scaling (faster fall-off)
                    val knockback = toTarget.multiply(knockStrength).setY(0.2 + 0.2 * knockScale)
                    entity.velocity = knockback
                    entity.fireTicks = 60
                    entity.damage(3.0, player)

                    world.spawnParticle(Particle.LAVA, entity.location.add(0.0, 1.0, 0.0), 10, 0.1, 0.1, 0.1, 0.01)
                }
            }
        }

        world.playSound(origin, Sound.ITEM_FIRECHARGE_USE, 1f, 1f)
    }
}
