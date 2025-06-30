package com.lukecywon.progressionPlus.commands

import org.bukkit.command.CommandExecutor

interface CustomCommand : CommandExecutor {
    val name: String
}