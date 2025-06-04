package com.lukecywon.progressionPlus

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ProgressionPlus : JavaPlugin() {
    override fun onEnable() {
        instance = this
        Initialize(this)
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
