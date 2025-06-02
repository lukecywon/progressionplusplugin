package com.lukecywon.progressionPlus

import org.bukkit.plugin.java.JavaPlugin

class ProgressionPlus : JavaPlugin() {

    override fun onEnable() {
        instance = this
        Initialize(this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var instance: ProgressionPlus

        fun getPlugin(): ProgressionPlus {
            return instance
        }
    }
}
