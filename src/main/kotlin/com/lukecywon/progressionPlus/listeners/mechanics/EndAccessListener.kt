package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.PortalCreateEvent
import org.bukkit.inventory.EquipmentSlot
import java.time.LocalDateTime
import java.time.ZoneId

class EndAccessListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()

    private fun isEndAccessible(): Pair<Boolean, LocalDateTime> {
        val config = plugin.config
        val unlockTimeStr = config.getString("end_unlock_time", "2099-01-01T00:00")
        val unlockTime = try {
            LocalDateTime.parse(unlockTimeStr)
        } catch (e: Exception) {
            LocalDateTime.MAX
        }

        val now = LocalDateTime.now(ZoneId.of("Asia/Kuala_Lumpur"))
        return Pair(now.isAfter(unlockTime), unlockTime)
    }

    @EventHandler
    fun onPortalCreate(event: PortalCreateEvent) {
        if (event.reason != PortalCreateEvent.CreateReason.END_PLATFORM) return

        val (accessible, unlockTime) = isEndAccessible()
        if (!accessible) {
            event.isCancelled = true
            val loc = event.entity?.location ?: return
            val nearby = loc.world?.getNearbyPlayers(loc, 5.0)
            nearby?.firstOrNull()?.sendMessage(
                "§5The End remains sealed.§r Wait until §e${unlockTime.toLocalDate()} ${unlockTime.toLocalTime()}§r."
            )
        }
    }

    @EventHandler
    fun onPlayerPortal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return

        val (accessible, unlockTime) = isEndAccessible()
        if (!accessible) {
            event.isCancelled = true
            event.player.sendMessage(
                "§5The End remains sealed.§r Wait until §e${unlockTime.toLocalDate()} ${unlockTime.toLocalTime()}§r."
            )
        }
    }

    @EventHandler
    fun onEyePlacement(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val block = event.clickedBlock ?: return
        if (block.type != Material.END_PORTAL_FRAME) return

        val (accessible, unlockTime) = isEndAccessible()
        if (!accessible) {
            val item = event.item ?: return
            if (item.type == Material.ENDER_EYE) {
                event.isCancelled = true
                event.player.sendMessage(
                    "§5The End is not yet open.§r Wait until §e${unlockTime.toLocalDate()} ${unlockTime.toLocalTime()}§r."
                )
            }
        }
    }
}
