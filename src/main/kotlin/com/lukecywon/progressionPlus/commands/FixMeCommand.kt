package com.lukecywon.progressionPlus.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FixMeCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return false

        sender.isInvulnerable = false
        sender.noDamageTicks = 0
        sender.maximumNoDamageTicks = 20
        sender.sendMessage("§aYou are now vulnerable again.")

        return true
    }
}
