package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.mechanics.MerchantsRequestManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MerchantsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.sendMessage("§7Usage: §e/trade <accept|reject>")
            return true
        }

        return when (args[0].lowercase()) {
            "accept" -> MerchantsRequestManager.accept(sender)
            "reject" -> MerchantsRequestManager.reject(sender)
            else -> {
                sender.sendMessage("§cInvalid option. Use /trade accept or /trade reject")
                true
            }
        }
    }
}
