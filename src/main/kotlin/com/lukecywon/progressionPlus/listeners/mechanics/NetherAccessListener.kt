package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class NetherAccessListener() : Listener {
    private val plugin = ProgressionPlus.getPlugin()

    @EventHandler
    fun onPortalCreate(event: PlayerPortalEvent) {
        if (event.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            val player = event.player

            val netherUnlocked = plugin.config.getBoolean("nether_unlocked", false)

            if (!netherUnlocked) {
                event.isCancelled = true
                player.sendMessage("§cThe Nether is sealed... Find the Nether Eye.")
            }
        }
    }
}
