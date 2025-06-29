package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.setup.RunSetup
import org.bukkit.plugin.java.JavaPlugin

class ProgressionPlus : JavaPlugin() {
    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        RunSetup(this)
        println("ProgressionPlus has been enabled.")
    }

    override fun onDisable() {
        println("ProgressionPlus has been disabled.")
    }

    companion object {
        lateinit var instance: ProgressionPlus

        fun getPlugin(): ProgressionPlus {
            return instance
        }
    }
}
