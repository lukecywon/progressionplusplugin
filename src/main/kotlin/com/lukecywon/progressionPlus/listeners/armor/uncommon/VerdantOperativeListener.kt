package com.lukecywon.progressionPlus.listeners.armor.uncommon

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VerdantOperativeListener : Listener {
    val key = NamespacedKey(ProgressionPlus.getPlugin(), "verdant_operative_set")

    @EventHandler
    fun onArmorChange(event: PlayerArmorChangeEvent) {
        val player = event.player

        Bukkit.getScheduler().runTaskLater(ProgressionPlus.instance, Runnable {
            if (isWearingFullVerdantOperativeSet(player)) {
                if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                    player.addPotionEffect(
                        PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 1, true, false)
                    )
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                }
            } else {
                player.removePotionEffect(PotionEffectType.SPEED)
            }
        }, 1L)
    }

    private fun isWearingFullVerdantOperativeSet(player: Player): Boolean {
        val armor = player.inventory.armorContents

        return armor.all { item ->
            item?.persistentDataContainer?.has(key, PersistentDataType.BYTE) ?: false
        }
    }
}