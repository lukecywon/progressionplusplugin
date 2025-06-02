package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.commands.ArtifactCommand
import com.lukecywon.progressionPlus.commands.ArtifactTabCompleter
import com.lukecywon.progressionPlus.listeners.EchoGunListener
import com.lukecywon.progressionPlus.recipes.EchoGunRecipe
import com.lukecywon.progressionPlus.recipes.Recipe
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Initialize(private val plugin: JavaPlugin) {
    init {
        commands()
        listeners()
        recipes()
    }

    private fun commands() {
        plugin.getCommand("artifact")?.setExecutor(ArtifactCommand())
        plugin.getCommand("artifact")?.tabCompleter = ArtifactTabCompleter()
    }

    private fun listeners() {
        val listeners = listOf(
            EchoGunListener()
        )

        listeners.forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    private fun recipes() {
        val listeners = listOf<Recipe>(
            EchoGunRecipe()
        )

        listeners.forEach {
            if (Bukkit.getRecipe(it.nameSpacedKey) != null) {
                Bukkit.removeRecipe(it.nameSpacedKey)
                Bukkit.addRecipe(it.getRecipe())
            } else {
                Bukkit.addRecipe(it.getRecipe())
            }
        }

    }
}