package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import com.lukecywon.progressionPlus.mechanics.*
import org.bukkit.plugin.java.JavaPlugin

class SetupManagers {
    @RunOnEnable
    private fun managers(plugin: JavaPlugin) {
        val mechanics = listOf<Manager>(
            BerserkerSwordManager,
            FlightBeaconManager,
            LegendaryManager,
            DiamondRecipeManager
        )

        mechanics.forEach {
            it.start(plugin)
        }
    }
}