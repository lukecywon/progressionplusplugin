package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.events.DayStartEvent
import com.lukecywon.progressionPlus.events.NightStartEvent
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object DayNightManager : Manager {
    private val worldState = mutableMapOf<World, Boolean>()

    override fun start(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                Bukkit.getWorlds().forEach { world ->
                    val time = world.time
                    val isDay = time in 0..12300 || time in 23000..23999
                    val wasDay = worldState[world]

                    if (wasDay != null && wasDay != isDay) {
                        worldState[world] = isDay

                        val event = if (isDay) DayStartEvent(world) else NightStartEvent(world)
                        Bukkit.getPluginManager().callEvent(event)
                    } else if (wasDay == null) {
                        worldState[world] = isDay
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 100L)
    }
}