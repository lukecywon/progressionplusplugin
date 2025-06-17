package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ConfigCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command can only be run by players.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /$label <subcommand>")
            return true
        }

        when (args[0].lowercase()) {
            "du" -> {
                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /$label give <item>")
                    return true
                }

                when (args[1].lowercase()) {
                    "true" -> {
                        ProgressionPlus.getPlugin().config.set("diamond-unlocked", true)
                    }
                    "false" -> {
                        ProgressionPlus.getPlugin().config.set("diamond-unlocked", false)
                    }
                }
            }
        }


        return true
    }
}