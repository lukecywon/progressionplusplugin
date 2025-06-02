package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.commands.ArtifactCommand
import com.lukecywon.progressionPlus.commands.ArtifactTabCompleter
import com.lukecywon.progressionPlus.listeners.*
import com.lukecywon.progressionPlus.listeners.MaxHeartFruitListener
import com.lukecywon.progressionPlus.listeners.LegendaryItemListener
import com.lukecywon.progressionPlus.mechanics.BerserkerSwordManager
import com.lukecywon.progressionPlus.mechanics.FlightBeaconManager
import com.lukecywon.progressionPlus.recipes.EchoGunRecipe
import com.lukecywon.progressionPlus.recipes.FlightBeaconRecipe
import com.lukecywon.progressionPlus.recipes.Recipe
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Initialize(private val plugin: JavaPlugin) {
    init {
        commands()
        listeners()
        recipes()
        mechanics()
    }

    private fun commands() {
        plugin.getCommand("artifact")?.setExecutor(ArtifactCommand())
        plugin.getCommand("artifact")?.tabCompleter = ArtifactTabCompleter()
    }

    private fun listeners() {
        val listeners = listOf(
            EchoGunListener(),
            LegendaryItemListener(),
            MaxHeartFruitListener(),
            BerserkerSwordListener(),
            FlightBeaconListener()
        )

        listeners.forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    private fun recipes() {
        val recipes = listOf<Recipe>(
            EchoGunRecipe(),
            FlightBeaconRecipe()
        )

        recipes.forEach {
            if (Bukkit.getRecipe(it.nameSpacedKey) != null) {
                Bukkit.removeRecipe(it.nameSpacedKey)
                Bukkit.addRecipe(it.getRecipe())
            } else {
                Bukkit.addRecipe(it.getRecipe())
            }
        }

    }

    private fun mechanics() {
        BerserkerSwordManager.startMonitorTask(plugin)
        FlightBeaconManager.startFlightChecker(plugin)
    }
}