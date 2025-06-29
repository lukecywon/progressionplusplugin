package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.CustomItem
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CooldownCommand : CustomCommand {
    override val name = "cooldown"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args == null || args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}Usage: /cooldown clear <player>")
            return true
        }

        if (args[0].equals("clear", ignoreCase = true)) {
            if (args.size < 2) {
                sender.sendMessage("${ChatColor.RED}Specify a player to clear cooldowns for.")
                return true
            }

            val target = Bukkit.getPlayerExact(args[1])
            if (target == null || !target.isOnline) {
                sender.sendMessage("${ChatColor.RED}Player not found or offline.")
                return true
            }

            CustomItem.clearCooldowns(target.uniqueId)
            sender.sendMessage("${ChatColor.GREEN}Cleared all cooldowns for ${target.name}.")
        } else {
            sender.sendMessage("${ChatColor.RED}Unknown subcommand. Use: /cooldown clear <player>")
        }

        return true
    }
}
