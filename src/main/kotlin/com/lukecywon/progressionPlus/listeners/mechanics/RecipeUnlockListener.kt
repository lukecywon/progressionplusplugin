package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.items.CustomItemRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class RecipeUnlockListener : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        for (item in CustomItemRegistry.getAll()) {
            e.player.discoverRecipe(item.key)
        }
    }

}