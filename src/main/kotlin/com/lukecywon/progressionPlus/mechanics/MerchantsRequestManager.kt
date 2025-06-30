@file:Suppress("DEPRECATION")

package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.gui.MerchantsTradeGUI
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object MerchantsRequestManager {
    private val pendingRequests = mutableMapOf<UUID, UUID>() // target → requester
    private val reverseLookup = mutableMapOf<UUID, UUID>() // requester → target

    fun requestTrade(requester: Player, target: OfflinePlayer) {
        if (target !is Player || !target.isOnline) {
            requester.sendMessage("§cThat player is not online.")
            return
        }

        if (pendingRequests.containsKey(target.uniqueId)) {
            requester.sendMessage("§cThat player already has a pending trade request.")
            return
        }

        if (reverseLookup.containsKey(requester.uniqueId)) {
            requester.sendMessage("§cYou already have a pending trade request.")
            return
        }

        pendingRequests[target.uniqueId] = requester.uniqueId
        reverseLookup[requester.uniqueId] = target.uniqueId
        sendTradeRequestMessage(target, requester)
        target.playSound(target.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        requester.sendMessage("§7Trade request sent to §b${target.name}§7.")

        // Set a timeout for 30 seconds
        object : BukkitRunnable() {
            override fun run() {
                if (pendingRequests.remove(target.uniqueId) != null) {
                    reverseLookup.remove(requester.uniqueId)
                    requester.sendMessage("§eYour trade request to §b${target.name} §ehas expired.")
                    if (target.isOnline) {
                        target.sendMessage("§7Trade request from §b${requester.name} §7has expired.")
                    }
                }
            }
        }.runTaskLater(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), 20L * 30)
    }

    fun hasRequestFor(target: UUID): Boolean = pendingRequests.containsKey(target)

    fun accept(target: Player): Boolean {
        val requesterId = pendingRequests.remove(target.uniqueId) ?: return false
        reverseLookup.remove(requesterId)
        val requester = Bukkit.getPlayer(requesterId) ?: return false

        requester.sendMessage("§a${target.name} accepted your trade request!")
        target.sendMessage("§aYou accepted the trade request.")
        requester.playSound(requester.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
        target.playSound(target.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)

        MerchantsTradeGUI.open(requester, target)
        return true
    }

    fun reject(target: Player): Boolean {
        val requesterId = pendingRequests.remove(target.uniqueId) ?: return false
        reverseLookup.remove(requesterId)
        val requester = Bukkit.getPlayer(requesterId) ?: return false

        requester.sendMessage("§c${target.name} rejected your trade request.")
        target.sendMessage("§7You rejected the trade request.")
        requester.playSound(requester.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.8f)
        target.playSound(target.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.8f)
        return true
    }

    private fun sendTradeRequestMessage(to: Player, from: Player) {
        val message = TextComponent("§6${from.name} wants to trade with you. §7(Expires in 30s)\n")

        val accept = TextComponent("§a[Accept]")
        accept.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade accept")
        accept.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§7Click to accept the trade"))

        val reject = TextComponent(" §c[Reject]")
        reject.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade reject")
        reject.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("§7Click to reject the trade"))

        message.addExtra(accept)
        message.addExtra(reject)

        to.spigot().sendMessage(message)
    }
}
