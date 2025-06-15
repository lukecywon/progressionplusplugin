package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.items.weapons.uncommon.BerserkerSword
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object BerserkerSwordManager : Manager {
    val originalHealth: MutableMap<UUID, Double> = mutableMapOf()
    private const val itemId = "berserker_sword"
    private const val cooldownMillis = 30_000L

    override fun start(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    val uuid = player.uniqueId
                    val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: continue
                    val isHolding = BerserkerSword.isBerserkerSword(player.inventory.itemInMainHand)
                    val wasHolding = originalHealth.containsKey(uuid)

                    if (isHolding && !wasHolding) {
                        originalHealth[uuid] = attr.baseValue
                        attr.baseValue = (attr.baseValue / 2).coerceAtLeast(2.0)
                    } else if (!isHolding && wasHolding) {
                        attr.baseValue = originalHealth.remove(uuid) ?: 20.0
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L)
    }

    fun handleRightClick(player: Player) {
        if (!BerserkerSword.isBerserkerSword(player.inventory.itemInMainHand)) return

        val uuid = player.uniqueId
        if (CustomItem.isOnCooldown(itemId, uuid)) {
            val remaining = CustomItem.getCooldownRemaining(itemId, uuid) / 1000
            player.sendMessage("§7You must wait §c${remaining}s§7 before unleashing your rage again!")
            return
        }

        player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 20 * 15, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.HUNGER, 20 * 15, 4))
        player.sendMessage("§cYou embrace the fury of battle!")

        CustomItem.setCooldown(itemId, uuid, cooldownMillis)
    }

    fun restoreHealthOnDeath(player: Player) {
        val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val original = originalHealth.remove(player.uniqueId) ?: return
        attr.baseValue = original
    }

    fun cleanup(player: Player) {
        val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val original = originalHealth.remove(player.uniqueId) ?: return
        attr.baseValue = original
    }

    fun isHalved(player: Player): Boolean {
        return originalHealth.containsKey(player.uniqueId)
    }
}
