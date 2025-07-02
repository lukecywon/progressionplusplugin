package com.lukecywon.progressionPlus.listeners.utility.progression

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.progression.NetherEye
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class NetherEyeListener : Listener {
    private val plugin = ProgressionPlus.getPlugin()
    private val config = plugin.config

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val player = e.player

        val item = player.inventory.itemInMainHand
        if (!NetherEye.isThisItem(item)) return

        // Nether eye only usable once
        if (config.getBoolean("nether_opened")) {
            player.sendMessage(Component.text("The Nether has already been unlocked.").color(NamedTextColor.DARK_RED))
            e.isCancelled = true
            return
        }

        // Trigger logic
        NetherEye.toggleNether(player)
        config.set("nether_opened", true)


        // Consume the item
        item.amount = item.amount - 1

        e.isCancelled = true
    }
}
