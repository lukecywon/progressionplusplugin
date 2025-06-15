package com.lukecywon.progressionPlus.listeners.weapons.rare

import com.lukecywon.progressionPlus.items.weapons.rare.AshenWarhammer
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.AreaEffectCloudApplyEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class AshenWarhammerListener : Listener {
    private val cloudSources = mutableMapOf<UUID, UUID>()  // cloud UUID → killer UUID
    private val itemId = "ashen_warhammer"
    private val cooldownMillis = 10_000L

    @EventHandler
    fun onKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        val weapon = killer.inventory.itemInMainHand
        if (!AshenWarhammer.isAshenWarhammer(weapon)) return

        if (CustomItem.isOnCooldown(itemId, killer.uniqueId)) return
        CustomItem.setCooldown(itemId, killer.uniqueId, cooldownMillis)

        val loc = e.entity.location
        val cloud = loc.world.spawn(loc, AreaEffectCloud::class.java).apply {
            radius = 3.0f
            duration = 100 // 5 seconds
            waitTime = 0
            setParticle(Particle.ASH)
            setRadiusPerTick(-0.05f)
            customName = "Ashen Weakness"
            addCustomEffect(PotionEffect(PotionEffectType.WITHER, 100, 1, false, true), true)
        }

        cloudSources[cloud.uniqueId] = killer.uniqueId
        loc.world.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 0.8f)
    }

    @EventHandler
    fun onCloudAffect(e: AreaEffectCloudApplyEvent) {
        val cloudId = e.entity.uniqueId
        val killerId = cloudSources[cloudId] ?: return
        e.affectedEntities.removeIf { it.uniqueId == killerId }
    }
}
