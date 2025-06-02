package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.EchoGun
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cUsage: /$label <subcommand>")
            return true
        }

        when (args[0].lowercase()) {
            "give" -> {
                if (sender !is Player) {
                    sender.sendMessage("§cOnly players can use this command.")
                    return true
                }

                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /$label give <item>")
                    return true
                }

                when (args[1].lowercase()) {
                    "echo_gun" -> {
                        sender.inventory.addItem(EchoGun.createItemStack())
                    }
                }

            }
        }

        return true
    }
}