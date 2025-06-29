package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import com.lukecywon.progressionPlus.mechanics.*
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class SetupManagers {
    @RunOnEnable
    private fun managers(plugin: JavaPlugin) {
        val managers = Reflections("com.lukecywon.progressionPlus.manager")
        val classes = managers.getSubTypesOf(Manager::class.java)

        classes.forEach {
            val classInstance = it.getDeclaredConstructor().newInstance()
            classInstance.start(plugin)
        }
    }
}