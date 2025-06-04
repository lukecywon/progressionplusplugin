package com.lukecywon.progressionPlus.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class CooldownTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        return when (args.size) {
            1 -> listOf("clear").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> Bukkit.getOnlinePlayers()
                .map { it.name }
                .filter { it.startsWith(args[1], ignoreCase = true) }
            else -> emptyList()
        }
    }
}
