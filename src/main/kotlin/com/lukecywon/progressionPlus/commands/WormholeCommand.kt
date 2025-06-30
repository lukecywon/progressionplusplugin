package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.mechanics.TeleportRequestManager
import net.kyori.adventure.text.event.ClickEvent.Payload.Custom
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class WormholeCommand : CustomCommand, CustomTabCompleter {
    override val name = "wormhole"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.sendMessage("§7Usage: §e/wormhole <accept|reject>")
            return true
        }

        val hasRequest = TeleportRequestManager.hasRequestFor(sender.uniqueId)
        if (!hasRequest) {
            sender.sendMessage("§cYou have no pending teleport requests.")
            return true
        }

        when (args[0].lowercase()) {
            "accept" -> TeleportRequestManager.accept(sender.uniqueId)
            "reject" -> TeleportRequestManager.reject(sender.uniqueId)
            else -> sender.sendMessage("§7Unknown subcommand. Use §e/wormhole <accept|reject>")
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1) {
            val options = listOf("accept", "reject")
            return options.filter { it.startsWith(args[0], ignoreCase = true) }
        }
        return emptyList()
    }
}
