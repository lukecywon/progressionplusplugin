package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.commands.ArtifactCommand
import com.lukecywon.progressionPlus.commands.ArtifactTabCompleter
import com.lukecywon.progressionPlus.commands.FixMeCommand
import com.lukecywon.progressionPlus.commands.WormholeCommand
import com.lukecywon.progressionPlus.listeners.*
import com.lukecywon.progressionPlus.mechanics.BerserkerSwordManager
import com.lukecywon.progressionPlus.mechanics.FlightBeaconManager
import com.lukecywon.progressionPlus.mechanics.Manager
import com.lukecywon.progressionPlus.items.*
import com.lukecywon.progressionPlus.recipes.*

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Initialize(private val plugin: JavaPlugin) {
    init {
        commands()
        listeners()
        recipes()
        mechanics()
        AllItems.registerAll()
    }

    private fun commands() {
        plugin.getCommand("artifact")?.setExecutor(ArtifactCommand())
        plugin.getCommand("artifact")?.tabCompleter = ArtifactTabCompleter()
        plugin.getCommand("fixme")?.setExecutor(FixMeCommand())
        plugin.getCommand("wormhole")?.setExecutor(WormholeCommand())
    }

    private fun listeners() {
        val listeners = listOf(
            EchoGunListener(),
            LegendaryItemListener(),
            MaxHeartFruitListener(),
            HealthCrystalListener(),
            BerserkerSwordListener(),
            FlightBeaconListener(),
            BannerListener(),
            RecallPotionListener(),
            PhoenixTotemListener(),
            WormholePotionListener(),
            WormholeGUIListener(),
            SnowGlobeListener(),
            HeartLossOnDeathListener(),
            RogueSwordListener(),
            VenomDaggerListener(),
            EchoBladeListener(),
            AshenWarhammerListener(),
            FerociousBladeListener(),
            SoulPiercerListener(),
            LuckTalismanListener(),
            VoidReaperListener(),
            SacrificialClockListener(),
        )

        listeners.forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    private fun recipes() {
        val recipes = listOf<Recipe>(
            EchoGunRecipe(),
            FlightBeaconRecipe(),
            HasteBannerRecipe(),
            JumpBannerRecipe(),
            AbsorptionBannerRecipe(),
            SpeedBannerRecipe(),
            RegenBannerRecipe(),
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
        val mechanics = listOf<Manager>(
            BerserkerSwordManager,
            FlightBeaconManager,
        )

        mechanics.forEach {
            it.start(plugin)
        }
    }
}