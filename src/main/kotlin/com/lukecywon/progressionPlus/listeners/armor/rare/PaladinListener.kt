package com.lukecywon.progressionPlus.listeners.armor.rare

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.events.DayStartEvent
import com.lukecywon.progressionPlus.events.NightStartEvent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PaladinListener : Listener {
    val key = NamespacedKey(ProgressionPlus.getPlugin(), "paladin_set")

    @EventHandler
    fun onArmorChange(event: PlayerArmorChangeEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(ProgressionPlus.instance, Runnable {
            if (isWearingFullPaladinSet(player)) {
                if (player.world.isDayTime) {
                    if (!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                        player.addPotionEffect(
                            PotionEffect(PotionEffectType.HEALTH_BOOST, Int.MAX_VALUE, 0, true, false)
                        )
                    }
                } else {
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.GLOWING, Int.MAX_VALUE, 1, true, false)
                    )
                }
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
            } else {
                player.removePotionEffect(PotionEffectType.HEALTH_BOOST)
                player.removePotionEffect(PotionEffectType.GLOWING)
            }
        }, 1L)
    }

    @EventHandler
    fun onDayTime(event: DayStartEvent) {
        event.world.players.forEach { player ->
            if (isWearingFullPaladinSet(player)) {
                if (!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.HEALTH_BOOST, Int.MAX_VALUE, 0, true, false)
                    )
                }
                player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f)
            }
        }
    }

    @EventHandler
    fun onDayTime(event: NightStartEvent) {
        event.world.players.forEach { player ->
            if (isWearingFullPaladinSet(player)) {
                if (!player.hasPotionEffect(PotionEffectType.HEALTH_BOOST)) {
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.GLOWING, Int.MAX_VALUE, 1, true, false)
                    )
                }
                player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f)
            }
        }
    }

    private fun isWearingFullPaladinSet(player: Player): Boolean {
        val armor = player.inventory.armorContents

        return armor.all { item ->
            item?.persistentDataContainer?.has(key, PersistentDataType.BYTE) ?: false
        }
    }
}