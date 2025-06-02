package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.EchoGun
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class EchoGunListener : Listener {
    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (EchoGun.isThisItem(e.player.inventory.itemInMainHand)) {
            EchoGun.shootSonicBoom(e.player)
            e.isCancelled = true
        }
    }
}