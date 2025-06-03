package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.SoulPiercer
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

class SoulPiercerListener : Listener {
    private val hitCounts = mutableMapOf<UUID, Int>()

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val attacker = e.damager as? Player ?: return
        val target = e.entity as? LivingEntity ?: return
        val item = attacker.inventory.itemInMainHand
        if (!SoulPiercer.isSoulPiercer(item)) return

        val uuid = attacker.uniqueId
        val hits = hitCounts.getOrDefault(uuid, 0) + 1
        hitCounts[uuid] = hits

        if (hits >= 5) {
            val baseDamage = e.damage

            // Cancel the default damage which would be affected by armor
            e.isCancelled = true

            // Deal "true damage" by setting health directly
            val healthBefore = target.health
            val trueDamage = baseDamage * 0.8
            val newHealth = (healthBefore - trueDamage).coerceAtLeast(0.0)

            target.health = newHealth
            target.noDamageTicks = 0  // Allow immediate damage handling

            // Feedback
            attacker.sendActionBar("§5✦ Soulpiercer! 80% Armor ignored!")
            attacker.world.playSound(attacker.location, Sound.ITEM_TRIDENT_THUNDER, 1f, 1.2f)
            attacker.world.spawnParticle(
                Particle.SOUL_FIRE_FLAME,
                target.location.add(0.0, 1.0, 0.0),
                15, 0.3, 0.5, 0.3, 0.05
            )

            hitCounts[uuid] = 0
        } else {
            attacker.sendActionBar("§7Soulpiercer: §d$hits§7/5")
        }
    }
}
