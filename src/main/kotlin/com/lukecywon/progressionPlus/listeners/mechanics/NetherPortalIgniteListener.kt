package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.PortalCreateEvent

class NetherPortalIgniteListener : Listener {

    @EventHandler
    fun onPortalCreate(event: PortalCreateEvent) {
        val config = ProgressionPlus.getPlugin().config
        if (!config.getBoolean("nether_unlocked", false)) {
            event.isCancelled = true

            // Try to notify nearest player (optional)
            val loc = event.entity?.location ?: return
            val nearby = loc.world?.getNearbyPlayers(loc, 5.0)
            nearby?.firstOrNull()?.sendMessage("§cThe Nether is sealed. Use the §4Nether Eye§c to unlock it.")
        }
    }
}
