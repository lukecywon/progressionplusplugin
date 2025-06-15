package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.mechanics.BerserkerSwordManager.originalHealth
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*

class HeartLossOnDeathListener : Listener {
    private val lowHealthDeathCounter = mutableMapOf<UUID, Int>()

    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val player = e.entity
        val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val id = player.uniqueId

        // ✅ Restore pre-Berserker health first
        val restored = originalHealth.remove(id)
        if (restored != null) {
            attr.baseValue = restored
        }

        val currentMax = attr.baseValue

        // ✅ Abort if already at minimum (5 hearts)
        if (currentMax <= 10.0) return

        // ✅ Reduce immediately if above 20.0
        if (currentMax > 20.0) {
            attr.baseValue = (currentMax - 2.0).coerceAtLeast(10.0)
            player.sendMessage("§cYou feel weaker after dying...")
            return
        }

        // ✅ Otherwise use 3-death logic
        val deaths = lowHealthDeathCounter.getOrDefault(id, 0) + 1
        if (deaths >= 3) {
            attr.baseValue = (currentMax - 2.0).coerceAtLeast(2.0)
            player.sendMessage("§cRepeated deaths have weakened you!")
            lowHealthDeathCounter[id] = 0
        } else {
            lowHealthDeathCounter[id] = deaths
            player.sendMessage("§eYou feel strained... (${deaths}/3)")
        }
    }
}
