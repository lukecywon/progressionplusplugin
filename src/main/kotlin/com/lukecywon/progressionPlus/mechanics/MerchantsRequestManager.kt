package com.lukecywon.progressionPlus.mechanics

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

object MerchantsRequestManager {
    private val pendingRequests = mutableMapOf<UUID, UUID>() // target → requester

    fun requestTrade(requester: Player, target: OfflinePlayer) {
        if (target !is Player || !target.isOnline) {
            requester.sendMessage("§cThat player is not online.")
            return
        }

        if (pendingRequests.containsKey(target.uniqueId)) {
            requester.sendMessage("§cThat player already has a pending trade request.")
            return
        }

        pendingRequests[target.uniqueId] = requester.uniqueId
        target.sendMessage("§6${requester.name} wants to trade with you.")
        target.sendMessage("§eType §a/trade accept §eto accept, or §c/trade reject §eto reject.")
        target.playSound(target.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        requester.sendMessage("§7Trade request sent to §b${target.name}§7.")
    }

    fun hasRequestFor(target: UUID): Boolean = pendingRequests.containsKey(target)

    fun accept(target: Player): Boolean {
        val requesterId = pendingRequests.remove(target.uniqueId) ?: return false
        val requester = Bukkit.getPlayer(requesterId) ?: return false

        requester.sendMessage("§a${target.name} accepted your trade request!")
        target.sendMessage("§aYou accepted the trade request.")
        requester.playSound(requester.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
        target.playSound(target.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)

        MerchantsTradeSessionManager.openTradeGUI(requester, target)
        return true
    }

    fun reject(target: Player): Boolean {
        val requesterId = pendingRequests.remove(target.uniqueId) ?: return false
        val requester = Bukkit.getPlayer(requesterId) ?: return false

        requester.sendMessage("§c${target.name} rejected your trade request.")
        target.sendMessage("§7You rejected the trade request.")
        requester.playSound(requester.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.8f)
        target.playSound(target.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.8f)
        return true
    }
}
