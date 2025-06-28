package com.lukecywon.progressionPlus.listeners.utility.uncommon

import com.lukecywon.progressionPlus.items.utility.uncommon.PhantomCharm
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent

class PhantomCharmListener : Listener {

    @EventHandler
    fun onPhantomTarget(e: EntityTargetEvent) {
        if (e.entity !is Phantom) return
        val player = e.target as? Player ?: return

        val offhand = player.inventory.itemInOffHand
        if (PhantomCharm.isThisItem(offhand)) {
            e.isCancelled = true
        }
    }
}
