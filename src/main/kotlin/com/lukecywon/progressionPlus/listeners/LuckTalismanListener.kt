package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.utility.legendary.LuckTalisman
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LuckTalismanListener : Listener {
    private val itemId = "luck_talisman"
    private val cooldownMillis = 5 * 60 * 1000L // 5 minutes

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!LuckTalisman.isLuckTalisman(item)) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val remaining = CustomItem.getCooldownRemaining(itemId, player.uniqueId) / 1000
            player.sendActionBar("§cLuck Talisman cooling down... §7(${remaining}s)")
            return
        }

        // Apply Luck IV for 15 seconds
        player.addPotionEffect(PotionEffect(PotionEffectType.LUCK, 15 * 20, 3))
        player.sendActionBar("§6You feel luckier!")

        // Totem pop sound + particle
        player.world.playSound(player.location, Sound.ITEM_TOTEM_USE, 1f, 1.2f)
        player.world.spawnParticle(Particle.TOTEM_OF_UNDYING, player.location.add(0.0, 1.0, 0.0), 30, 0.4, 0.5, 0.4, 0.2)

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
        e.isCancelled = true
    }
}
