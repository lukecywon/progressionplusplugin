package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class SetupListeners {
    @RunOnEnable
    fun listeners(plugin: JavaPlugin) {
        val listeners = Reflections("com.lukecywon.progressionPlus.listeners")
        val classes = listeners.getSubTypesOf(Listener::class.java)

        classes.forEach { listener ->
            plugin.server.pluginManager.registerEvents(listener.getDeclaredConstructor().newInstance(), plugin)
        }
    }
}