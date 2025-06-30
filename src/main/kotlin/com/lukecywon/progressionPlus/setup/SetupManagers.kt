package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.utils.annotations.RunOnEnable
import com.lukecywon.progressionPlus.mechanics.*
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class SetupManagers {
    @RunOnEnable
    fun managers(plugin: JavaPlugin) {
        val managers = Reflections("com.lukecywon.progressionPlus.mechanics")
        val classes = managers.getSubTypesOf(Manager::class.java)

        classes.forEach {
            val classObject = it.kotlin.objectInstance!!
            classObject.start(plugin)
        }
    }
}