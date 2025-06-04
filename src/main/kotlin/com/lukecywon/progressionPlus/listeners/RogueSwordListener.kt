package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.RogueSword
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class RogueSwordListener : Listener {
    private val itemId = "rogue_sword"
    private val cooldownMillis = 20_000L

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!RogueSword.isRogueSword(item)) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cRogue Sword is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        // Apply Speed I for 10 seconds
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 0))
        player.sendMessage("§aYou feel a surge of speed!")
        player.playSound(player.location, Sound.ENTITY_BAT_TAKEOFF, 1f, 1.5f)

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
        e.isCancelled = true
    }
}
