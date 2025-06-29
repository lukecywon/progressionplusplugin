package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.CustomItemRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true

        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /$label <subcommand>")
            return true
        }

        when (args[0].lowercase()) {
            "give" -> {
                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /$label give <item>")
                    return true
                }

                val item = CustomItem.getItem(args[1])
                if (item != null) {
                    sender.inventory.addItem(item.createItemStack())
                    sender.sendMessage("§aGave you: ${args[1]}")
                } else {
                    sender.sendMessage("§cUnknown item: ${args[1]}")
                }
            }
        }

        return true
    }
}