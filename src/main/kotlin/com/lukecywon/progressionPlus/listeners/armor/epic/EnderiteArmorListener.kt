package com.lukecywon.progressionPlus.listeners.armor.epic

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.lukecywon.progressionPlus.ProgressionPlus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class EnderiteArmorListener : Listener {
    private val key = NamespacedKey(ProgressionPlus.getPlugin(), "enderite_set")
    private val hitCount: MutableMap<UUID, Int> = mutableMapOf()
    private val maxHits = 5

    @EventHandler
    fun onArmorChange(event: PlayerArmorChangeEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(ProgressionPlus.instance, Runnable {
            if (isWearingFullEnderiteSet(player)) {
                if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, true, false)
                    )
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.HEALTH_BOOST, Int.MAX_VALUE, 1, true, false)
                    )
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                }
            } else {
                player.removePotionEffect(PotionEffectType.SPEED)
                player.removePotionEffect(PotionEffectType.HEALTH_BOOST)
            }
        }, 1L)
    }

    @EventHandler
    fun onPlayerDamaged(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (!isWearingFullEnderiteSet(player)) return

        val uuid = player.uniqueId
        val count = hitCount.getOrDefault(uuid, 0) + 1

        player.sendActionBar(Component.text("⛨ Hit counter: $count/$maxHits", NamedTextColor.LIGHT_PURPLE))

        if (count >= maxHits) {
            // Dodge this hit
            event.isCancelled = true
            hitCount[uuid] = 0

            player.world.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
            player.spawnParticle(Particle.PORTAL, player.location, 30, 0.5, 1.0, 0.5, 0.1)
            player.sendActionBar(Component.text("⛨ Dodged!", NamedTextColor.LIGHT_PURPLE))
        } else {
            hitCount[uuid] = count
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        hitCount.remove(event.player.uniqueId)
    }

    private fun isWearingFullEnderiteSet(player: Player): Boolean {
        val armor = player.inventory.armorContents

        return armor.all { item ->
            item?.persistentDataContainer?.has(key, PersistentDataType.BYTE) ?: false
        }
    }
}