package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.Initialize
import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object DiamondRecipeManager : Manager {
    override fun start(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                val plugin = ProgressionPlus.getPlugin()
                val diamondUnlocked = plugin.config.getBoolean("diamond-unlocked")

                if (diamondUnlocked) {
                    Initialize.savedRecipies.forEach { recipe ->
                        Bukkit.addRecipe(recipe)
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 0L)
    }
}