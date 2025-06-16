package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent
import org.bukkit.event.player.PlayerJoinEvent

class AdvancementRecipeUnlockListener : Listener {
    private val plugin = ProgressionPlus.getPlugin()
    private val killDragon = NamespacedKey.minecraft("end/kill_dragon")

    private val diamondRecipes = listOf(
        NamespacedKey.minecraft("diamond_sword"),
        NamespacedKey.minecraft("diamond_pickaxe"),
        NamespacedKey.minecraft("diamond_axe"),
        NamespacedKey.minecraft("diamond_shovel"),
        NamespacedKey.minecraft("diamond_hoe"),
        NamespacedKey.minecraft("diamond_helmet"),
        NamespacedKey.minecraft("diamond_chestplate"),
        NamespacedKey.minecraft("diamond_leggings"),
        NamespacedKey.minecraft("diamond_boots")
    )

    @EventHandler
    fun onAdvancementDone(e: PlayerAdvancementDoneEvent) {
        if (e.advancement.key != killDragon) return
        if (plugin.config.getBoolean("diamond-unlocked")) return

        plugin.config.set("diamond-unlocked", true)
        plugin.saveConfig()

        for (player in Bukkit.getOnlinePlayers()) {
            player.discoverRecipes(diamondRecipes)
            player.sendMessage("§b🗡 Diamond crafting unlocked! The Ender Dragon has been defeated!")
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (plugin.config.getBoolean("diamond-unlocked")) {
            e.player.discoverRecipes(diamondRecipes)
        }
    }
}

