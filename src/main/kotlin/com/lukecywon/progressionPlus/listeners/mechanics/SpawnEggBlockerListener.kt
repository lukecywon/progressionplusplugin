package com.lukecywon.progressionPlus.listeners.mechanics

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class SpawnEggBlockerListener : Listener {

    @EventHandler
    fun onSpawnEggUse(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val block: Block = event.clickedBlock ?: return
        val item = event.item ?: return

        if (block.type == Material.SPAWNER && item.type.name.endsWith("_SPAWN_EGG")) {
            event.isCancelled = true
            event.player.sendMessage("§cYou can't use spawn eggs on spawners.")
        }
    }
}
