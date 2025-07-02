package com.lukecywon.progressionPlus.listeners.armor.rare

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.SetBonusHelper
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
    private val plugin = ProgressionPlus.getPlugin()
    private val key = NamespacedKey(plugin, "verdant_operative_set")

    @EventHandler
    fun onArmorChange(event: PlayerArmorChangeEvent) {
        val player = event.player
        val uuid = player.uniqueId

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val isWearing = isWearingFullVerdantOperativeSet(player)
            val currentSet = SetBonusHelper.activeSet[uuid]

            if (isWearing) {
                if (currentSet != key) {
                    player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 0, true, false))
                    player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
                    SetBonusHelper.activeSet[uuid] = key
                }
            } else if (currentSet == key) {
                player.removePotionEffect(PotionEffectType.SPEED)
                SetBonusHelper.activeSet.remove(uuid)
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