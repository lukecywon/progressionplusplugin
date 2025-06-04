package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.commands.WormholeCommand
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object TeleportRequestManager : Manager {
    private val requests = mutableMapOf<UUID, PendingTeleport>()
    private const val itemId = "wormhole_potion"
    private const val cooldownMillis = 35_000L

    override fun start(plugin: JavaPlugin) {
        plugin.getCommand("wormhole")?.setExecutor(WormholeCommand())
    }

    fun createRequest(
        requester: Player,
        target: Player,
        potion: ItemStack,
        plugin: JavaPlugin
    ) {
        val pending = PendingTeleport(requester, target, System.currentTimeMillis() + 30_000, potion)
        requests[target.uniqueId] = pending

        CustomItem.setCooldown(itemId, requester.uniqueId, cooldownMillis)

        target.sendMessage("§d✧ §b${requester.name} §7wants to teleport to you.")
        target.sendMessage("§7Type §a/wormhole accept §7or §c/wormhole reject §7within §e30 seconds§7.")

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val expired = requests.remove(target.uniqueId)
            if (expired != null) {
                refundPotion(expired.requester, expired.potionItem)
                expired.requester.sendMessage("§c✗ Teleport request expired. Your Wormhole Potion has been returned.")
                target.sendMessage("§7⌛ Teleport request from §b${expired.requester.name} §7has §cexpired§7.")
                target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.7f, 0.5f)
            }
        }, 30 * 20L)
    }

    fun accept(targetUUID: UUID) {
        val req = requests.remove(targetUUID) ?: return
        if (System.currentTimeMillis() > req.expireTime) {
            refundPotion(req.requester, req.potionItem)
            return
        }

        req.requester.teleport(req.target.location)
        req.requester.sendMessage("§aTeleport successful!")
        req.target.sendMessage("§aYou accepted the teleport request.")
    }

    fun reject(targetUUID: UUID) {
        val req = requests.remove(targetUUID) ?: return
        if (System.currentTimeMillis() > req.expireTime) {
            refundPotion(req.requester, req.potionItem)
            return
        }

        refundPotion(req.requester, req.potionItem)
        req.requester.sendMessage("§cTeleport request was rejected. Your potion has been returned.")
        req.target.sendMessage("§eYou rejected the teleport request.")
    }

    fun hasRequestFor(uuid: UUID): Boolean {
        return requests.containsKey(uuid)
    }

    fun hasOutgoingRequestFrom(requesterUUID: UUID): Boolean {
        return requests.values.any { it.requester.uniqueId == requesterUUID }
    }

    fun isOnCooldown(uuid: UUID): Boolean {
        return CustomItem.isOnCooldown(itemId, uuid)
    }

    fun secondsLeft(uuid: UUID): Int {
        val millisLeft = CustomItem.getCooldownRemaining(itemId, uuid)
        return (millisLeft / 1000).toInt()
    }

    private fun refundPotion(player: Player, potion: ItemStack) {
        // Refund potion
        val leftover = player.inventory.addItem(potion)
        if (leftover.isNotEmpty()) {
            leftover.values.forEach { player.world.dropItemNaturally(player.location, it) }
        }

        // Remove one glass bottle
        val glass = player.inventory.contents.firstOrNull { it?.type == Material.GLASS_BOTTLE }
        glass?.let {
            it.amount -= 1
            if (it.amount <= 0) player.inventory.remove(it)
        }
    }
}
