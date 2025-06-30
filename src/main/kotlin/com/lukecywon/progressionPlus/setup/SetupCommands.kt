package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.utils.annotations.RunOnEnable
import com.lukecywon.progressionPlus.commands.*
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class SetupCommands {
    private val commands = Reflections("com.lukecywon.progressionPlus.commands")

    @RunOnEnable
    fun commands(plugin: JavaPlugin) {
        val classes = commands.getSubTypesOf(CustomCommand::class.java)

        classes.forEach { cls ->
            val clsInstance = cls.getDeclaredConstructor().newInstance()
            plugin.getCommand(clsInstance.name)?.setExecutor(clsInstance)
        }
    }

    @RunOnEnable
    fun tabCompleters(plugin: JavaPlugin) {
        val classes = commands.getSubTypesOf(CustomTabCompleter::class.java)

        classes.forEach { cls ->
            val clsInstance = cls.getDeclaredConstructor().newInstance()
            plugin.getCommand(clsInstance.name)?.tabCompleter = clsInstance
        }
    }
}