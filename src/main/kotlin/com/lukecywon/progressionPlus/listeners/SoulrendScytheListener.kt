package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.SoulrendScythe
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.potion.PotionEffectType

class SoulrendScytheListener : Listener {

    companion object {
        private val NEGATIVE_EFFECTS = setOf(
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.BLINDNESS,
            PotionEffectType.SLOWNESS,
            PotionEffectType.WEAKNESS,
            PotionEffectType.UNLUCK,
            PotionEffectType.HUNGER,
            PotionEffectType.MINING_FATIGUE,
            PotionEffectType.DARKNESS,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.INFESTED,
            PotionEffectType.LEVITATION,
            PotionEffectType.NAUSEA,
            PotionEffectType.OOZING,
            PotionEffectType.RAID_OMEN,
            PotionEffectType.WIND_CHARGED,
            PotionEffectType.WEAVING,
            PotionEffectType.TRIAL_OMEN
        )
    }

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val player = e.damager as? Player ?: return

        // Ignore "fake" sweeping hits
        if (e.entity.hasMetadata("soulrend_fake_hit")) return
        val target = e.entity as? LivingEntity ?: return
        val item = player.inventory.itemInMainHand

        if (!SoulrendScythe.isThisItem(item)) return

        // === Calculate bonus damage from negative effects ===
        val bonusDamage = player.activePotionEffects
            .filter { it.type in NEGATIVE_EFFECTS }
            .sumOf { it.amplifier + 1 }

        val baseDamage = 5.0
        val totalDamage = baseDamage + bonusDamage
        e.damage = totalDamage

        // === Lifesteal 40% of damage ===
        val healAmount = (totalDamage * 0.25).coerceAtLeast(1.0)
        player.health = (player.health + healAmount).coerceAtMost(player.maxHealth)
        player.world.spawnParticle(Particle.HEART, player.location.add(0.0, 1.0, 0.0), 3, 0.3, 0.2, 0.3)
        player.sendActionBar("§c❤ Lifesteal: +${healAmount.toInt()} HP")

        // === Fake sweeping edge ===
        val radius = 3.0
        val height = 1.5
        val world = player.world

        world.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f)
        world.spawnParticle(Particle.SWEEP_ATTACK, target.location.add(0.0, 1.0, 0.0), 1)

        world.getNearbyEntities(player.location, radius, height, radius).forEach {
            if (it is LivingEntity && it != player && it != target) {
                it.setMetadata("soulrend_fake_hit", FixedMetadataValue(ProgressionPlus.getPlugin(), true))
                it.damage(totalDamage * 0.5, player)
                it.removeMetadata("soulrend_fake_hit", ProgressionPlus.getPlugin())
                world.spawnParticle(Particle.DAMAGE_INDICATOR, it.location.add(0.0, 1.0, 0.0), 5)
            }
        }
    }
}
