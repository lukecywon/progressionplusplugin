package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.weapons.legendary.VoidReaper
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AddSoulsCommand : CustomCommand {
    override val name: String = "addsouls"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("This command can only be run by players.")
            return true
        }

        val item = sender.inventory.itemInMainHand
        if (!VoidReaper.isThisItem(item)) {
            sender.sendMessage("§cYou're not holding the Void Reaper.")
            return true
        }

        val amount = args.getOrNull(0)?.toIntOrNull() ?: 1
        repeat(amount.coerceAtMost(30)) {
            VoidReaper.addSoul(item)
        }

        sender.sendMessage("§aAdded $amount soul(s) to your Void Reaper.")
        return true
    }
}
