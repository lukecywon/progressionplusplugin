package com.lukecywon.progressionPlus.listeners.armor.epic

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class FrostboundArmorListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()
    private val frostKey = NamespacedKey(plugin, "frostbound_set")
    private val itemId = "frostbound_armor" // Used as cooldown key in CustomItem
    private val cooldownMillis = 15_000L     // 15 seconds cooldown
    private val effectDuration = 8 * 20L     // 8 seconds in ticks
    private val damageInterval = 70L         // 3.5 seconds in ticks

    private fun isFrostbound(item: ItemStack?): Boolean {
        val meta = item?.itemMeta ?: return false
        return meta.persistentDataContainer.has(frostKey, PersistentDataType.BYTE)
    }

    private fun isWearingFullFrostboundSet(player: Player): Boolean {
        return player.inventory.armorContents.all { isFrostbound(it) }
    }

    @EventHandler
    fun onArmorChange(event: PlayerArmorChangeEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(ProgressionPlus.instance, Runnable {
            if (isWearingFullFrostboundSet(player)) {
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            }
        }, 1L)
    }

    @EventHandler
    fun onPlayerDamaged(event: EntityDamageByEntityEvent) {
        val victim = event.entity as? Player ?: return
        val attacker = event.damager as? LivingEntity ?: return
        if (!isWearingFullFrostboundSet(victim)) return

        // Check cooldown for the player wearing the armor
        if (CustomItem.isOnCooldown(itemId, victim.uniqueId)) return

        // Start cooldown
        CustomItem.setCooldown(itemId, victim.uniqueId, cooldownMillis)

        // Apply effects to attacker
        attacker.world.playSound(attacker.location, Sound.ENTITY_PLAYER_HURT_FREEZE, 1f, 0.6f)
        attacker.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, effectDuration.toInt(), 0))
        attacker.freezeTicks = 160

        attacker.world.spawnParticle(Particle.SNOWFLAKE, attacker.location.add(0.0, 1.0, 0.0), 15, 0.3, 0.6, 0.3, 0.01)
        attacker.world.playSound(attacker.location, Sound.BLOCK_SNOW_BREAK, 1f, 0.9f)

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (!attacker.isValid || attacker.isDead) {
                    cancel()
                    return
                }
                when (ticks) {
                    1, 2 -> attacker.damage(1.0)
                }
                ticks++
                if (ticks > 2) cancel()
            }
        }.runTaskTimer(plugin, damageInterval, damageInterval) // Starts ticking after 3.5s, runs every 3.5s
    }
}
