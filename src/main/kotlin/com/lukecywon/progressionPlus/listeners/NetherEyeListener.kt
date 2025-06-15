package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.utility.legendary.NetherEye
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class NetherEyeListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val player = e.player

        val item = player.inventory.itemInMainHand
        if (!NetherEye.isThisItem(item)) return

        // Trigger logic
        NetherEye.toggleNether(player)

        // Consume the item
        item.amount = item.amount - 1

        e.isCancelled = true
    }
}
