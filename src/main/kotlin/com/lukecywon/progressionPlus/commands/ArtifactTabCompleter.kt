package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ArtifactTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return when (args.size) {
            1 -> listOf("give").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> if (args[0].equals("give", ignoreCase = true)) {
                CustomItem.getAllNames().filter {
                    it.startsWith(args[1], ignoreCase = true)
                }
            } else emptyList()
            else -> emptyList()
        }
    }
}