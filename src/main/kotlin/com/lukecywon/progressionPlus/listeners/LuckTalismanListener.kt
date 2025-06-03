package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.LuckTalisman
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class LuckTalismanListener : Listener {
    private val cooldowns = mutableMapOf<UUID, Long>()
    private val cooldownTimeMillis = 5 * 60 * 1000L // 5 minutes

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        val player = e.player
        val item = player.inventory.itemInMainHand
        if (!LuckTalisman.isLuckTalisman(item)) return

        val uuid = player.uniqueId
        val now = System.currentTimeMillis()
        val lastUsed = cooldowns.getOrDefault(uuid, 0L)

        if (now - lastUsed < cooldownTimeMillis) {
            val remaining = (cooldownTimeMillis - (now - lastUsed)) / 1000
            player.sendActionBar("§cLuck Talisman cooling down... §7(${remaining}s)")
            return
        }

        // Apply Luck II for 15 seconds
        player.addPotionEffect(PotionEffect(PotionEffectType.LUCK, 15 * 20, 1))
        player.sendActionBar("§6You feel luckier!")

        // Set cooldown
        cooldowns[uuid] = now

        e.isCancelled = true
    }
}
