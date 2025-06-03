package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.SoulPiercer
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
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
            e.damage += calculateIgnoredArmorDamage(e.finalDamage, e.entity) // or just a flat bonus

            attacker.sendActionBar("§5✦ Soulpiercer! 80% armor ignored!")
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

    private fun calculateIgnoredArmorDamage(base: Double, target: org.bukkit.entity.Entity): Double {
        val living = target as? org.bukkit.entity.LivingEntity ?: return base
        val armor = living.getAttribute(Attribute.ARMOR)?.value ?: 0.0
        val toughness = living.getAttribute(Attribute.ARMOR_TOUGHNESS)?.value ?: 0.0

        val reduction = (armor * 0.04) / (1 + armor * 0.04)  // Approximate formula
        return base / (1 - reduction).coerceAtLeast(1.0)
    }
}
