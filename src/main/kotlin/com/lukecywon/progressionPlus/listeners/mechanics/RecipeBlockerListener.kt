package com.lukecywon.progressionPlus.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRecipeDiscoverEvent

class RecipeBlockerListener : Listener {

    private val diamondRecipes = setOf(
        "diamond_sword", "diamond_pickaxe", "diamond_axe", "diamond_shovel", "diamond_hoe",
        "diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots"
    )

    @EventHandler
    fun onRecipeDiscover(e: PlayerRecipeDiscoverEvent) {
        val key = e.recipe.key
        if (key in diamondRecipes) {
            e.isCancelled = true
        }
    }
}
