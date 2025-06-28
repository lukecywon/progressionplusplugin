package com.lukecywon.progressionPlus.listeners.weapons.legendary

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.legendary.GravityMaul
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class GravityMaulListener : Listener {
    private val itemId = "gravity_maul"
    private val cooldownMillis = 20_000L

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!GravityMaul.isThisItem(item)) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cGravity Maul is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        GravityMaul.slam(player)
        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
        e.isCancelled = true
    }
}
