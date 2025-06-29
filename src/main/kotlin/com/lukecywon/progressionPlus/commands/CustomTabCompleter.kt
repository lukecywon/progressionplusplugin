package com.lukecywon.progressionPlus.commands

import org.bukkit.command.TabCompleter

interface CustomTabCompleter : TabCompleter {
    val name: String
}