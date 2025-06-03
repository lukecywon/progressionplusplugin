package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.RogueSword
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class RogueSwordListener : Listener {
    private val cooldowns = mutableMapOf<UUID, Long>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!RogueSword.isRogueSword(item)) return

        val now = System.currentTimeMillis()
        val cooldownEnd = cooldowns[player.uniqueId] ?: 0L
        if (now < cooldownEnd) {
            val secondsLeft = ((cooldownEnd - now) / 1000).toInt()
            player.sendMessage("§cRogue Sword is on cooldown for §e$secondsLeft§c more second(s)!")
            return
        }

        // Apply Speed I for 10 seconds
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 0))
        player.sendMessage("§aYou feel a surge of speed!")
        player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 1.5f)

        cooldowns[player.uniqueId] = now + 15_000
        e.isCancelled = true
    }
}
