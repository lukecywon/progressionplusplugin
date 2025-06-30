package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.PortalCreateEvent
import java.time.LocalDateTime
import java.time.ZoneId

class NetherAccessListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()

    private fun isNetherAccessible(): Pair<Boolean, LocalDateTime> {
        val config = plugin.config
        val unlocked = config.getBoolean("nether_unlocked", false)
        val unlockTimeStr = config.getString("nether_unlock_time", "2025-07-01T00:22")
        val unlockTime = try {
            LocalDateTime.parse(unlockTimeStr)
        } catch (e: Exception) {
            LocalDateTime.MAX
        }

        val now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
        return Pair(unlocked && now.isAfter(unlockTime), unlockTime)
    }

    @EventHandler
    fun onPortalCreate(event: PortalCreateEvent) {
        val (accessible, unlockTime) = isNetherAccessible()
        if (!accessible) {
            event.isCancelled = true
            val loc = event.entity?.location ?: return
            val nearby = loc.world?.getNearbyPlayers(loc, 5.0)
            nearby?.firstOrNull()?.sendMessage("§cThe Nether is sealed. Use the §4Nether Eye§c and wait until §e${unlockTime.toLocalDate()} ${unlockTime.toLocalTime()}.")
        }
    }

    @EventHandler
    fun onPlayerPortal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return

        val (accessible, unlockTime) = isNetherAccessible()
        if (!accessible) {
            event.isCancelled = true
            event.player.sendMessage("§cThe Nether is sealed. Use the §4Nether Eye§c and wait until §e${unlockTime.toLocalDate()} ${unlockTime.toLocalTime()}.")
        }
    }
}
