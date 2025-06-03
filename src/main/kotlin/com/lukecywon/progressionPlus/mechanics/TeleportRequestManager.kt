package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.commands.WormholeCommand
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object TeleportRequestManager : Manager {
    private val requests = mutableMapOf<UUID, PendingTeleport>()
    private val cooldowns = mutableMapOf<UUID, Long>()

    override fun start(plugin: JavaPlugin) {
        // Register commands here
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

        // ✅ Set cooldown here (35s)
        cooldowns[requester.uniqueId] = System.currentTimeMillis() + 35_000

        // Send message
        target.sendMessage("§d✧ §b${requester.name} §7wants to teleport to you.")
        target.sendMessage("§7Type §a/wormhole accept §7or §c/wormhole reject §7within §e30 seconds§7.")


        // Timeout
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (requests.remove(target.uniqueId) != null) {
                val bottle = requester.inventory.contents.firstOrNull { it?.type == Material.GLASS_BOTTLE }
                bottle?.apply {
                    amount -= 1
                    if (amount <= 0) requester.inventory.remove(this)
                }
                requester.inventory.addItem(potion)
                requester.sendMessage("§c✗ Teleport request expired. Your Wormhole Potion has been returned.")
                target.sendMessage("§7⌛ Teleport request from §b${requester.name} §7has §cexpired§7.")
                target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.7f, 0.5f)
            }
        }, 30 * 20L)
    }

    fun accept(uuid: UUID) {
        val req = requests.remove(uuid) ?: return
        if (System.currentTimeMillis() > req.expireTime) return

        req.requester.teleport(req.target.location)
        req.requester.sendMessage("§aTeleport successful!")
        req.target.sendMessage("§aYou accepted the teleport request.")
    }
//    fun accept(uuid: UUID) {
//        val req = requests.remove(uuid) ?: return
//        if (System.currentTimeMillis() > req.expireTime) return
//
//        if (req.requester == req.target && req.teleportToSpawn) {
//            val spawn = req.requester.bedSpawnLocation ?: req.requester.world.spawnLocation
//            req.requester.teleportAsync(spawn)
//            req.requester.sendMessage("§aTeleported to spawn via Wormhole Potion!")
//            return
//        }
//
//        req.requester.teleport(req.target.location)
//        req.requester.sendMessage("§aTeleport successful!")
//        req.target.sendMessage("§aYou accepted the teleport request.")
//    }

    fun reject(uuid: UUID) {
        val req = requests.remove(uuid) ?: return
        if (System.currentTimeMillis() > req.expireTime) return

        req.requester.inventory.addItem(req.potionItem)
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
        val now = System.currentTimeMillis()
        return (cooldowns[uuid] ?: 0L) > now
    }

    fun secondsLeft(uuid: UUID): Int {
        val now = System.currentTimeMillis()
        val cooldownEnd = cooldowns[uuid] ?: return 0
        return ((cooldownEnd - now) / 1000).toInt()
    }
}
