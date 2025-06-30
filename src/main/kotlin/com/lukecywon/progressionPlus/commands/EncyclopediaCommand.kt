package com.lukecywon.progressionPlus.commands

import com.lukecywon.progressionPlus.items.utility.common.ItemEncyclopedia
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class EncyclopediaCommand : CustomCommand {
    override val name: String = "encyclopedia"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return true
        }

        val player = sender

        val hasEncyclopedia = player.inventory.contents.any { item ->
            item != null && ItemEncyclopedia.isThisItem(item)
        }

        if (hasEncyclopedia) {
            player.sendMessage("§cYou already have the Item Encyclopedia in your inventory.")
            return true
        }

        player.inventory.addItem(ItemEncyclopedia.createItemStack())
        player.sendMessage("§aYou received the §6Item Encyclopedia§a.")
        return true
    }
}
