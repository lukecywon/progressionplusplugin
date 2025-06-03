package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.mechanics.TeleportRequestManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.scheduler.BukkitRunnable

object WormholeGUI {
    private val activeGUIs = mutableMapOf<Player, ItemStack>() // player → potion (to refund if needed)

    fun open(requester: Player, potionItem: ItemStack): Boolean {
        val players = Bukkit.getOnlinePlayers().filter { it.uniqueId != requester.uniqueId }

        if (players.isEmpty()) {
            requester.sendMessage("§cNo other players online. The potion was returned.")
            requester.playSound(requester.location, Sound.ENTITY_VILLAGER_NO, 0.6f, 1.0f)

            // Refund the potion
            val leftover = requester.inventory.addItem(potionItem)
            if (leftover.isNotEmpty()) {
                leftover.values.forEach { requester.world.dropItemNaturally(requester.location, it) }
            }

            // Simulate used bottle
            val bottle = requester.inventory.contents.firstOrNull { it?.type == Material.GLASS_BOTTLE }
            bottle?.apply {
                amount -= 1
                if (amount <= 0) requester.inventory.remove(this)
            }

            return false
        }

        val guiSize = ((players.size - 1) / 9 + 1) * 9
        val inventory = Bukkit.createInventory(null, guiSize, "Wormhole Teleport")

        for (target in players) {
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            meta.owningPlayer = target
            meta.displayName(Component.text("§b${target.name}"))
            meta.lore(listOf(Component.text("§7Click to request teleport.")))
            head.itemMeta = meta
            inventory.addItem(head)
        }

        val cancel = ItemStack(Material.BARRIER)
        val meta = cancel.itemMeta
        meta.displayName(Component.text("§cCancel"))
        meta.lore(listOf(Component.text("§7Click to cancel and get your potion back.")))
        cancel.itemMeta = meta

        // Place cancel button in the center of the last row
        val centerSlot = inventory.size - 5
        inventory.setItem(centerSlot, cancel)

        activeGUIs[requester] = potionItem
        requester.openInventory(inventory)
        return true
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != "Wormhole Teleport") return
        e.isCancelled = true
        val clicked = e.currentItem ?: return

        if (clicked.type == Material.BARRIER) {
            player.closeInventory()
            player.sendMessage("§eWormhole request canceled. Potion refunded.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.6f, 1.2f)

            val potion = activeGUIs.remove(player)
            potion?.let {
                val leftover = player.inventory.addItem(it)
                if (leftover.isNotEmpty()) {
                    leftover.values.forEach { item -> player.world.dropItemNaturally(player.location, item) }
                }
            }

            return
        }

        val meta = clicked.itemMeta as? SkullMeta ?: return

        val offline = meta.owningPlayer ?: return
        val targetUUID = offline.uniqueId
        val target = Bukkit.getPlayer(targetUUID) ?: return

        val potion = activeGUIs.remove(player) ?: return
        player.closeInventory()

        if (TeleportRequestManager.hasRequestFor(targetUUID)) {
            player.sendMessage("§cThat player already has a pending teleport request.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6f, 0.5f)
            return
        }

        if (TeleportRequestManager.hasOutgoingRequestFrom(player.uniqueId)) {
            player.sendMessage("§cYou already have a pending teleport request.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6f, 0.5f)
            return
        }

        TeleportRequestManager.createRequest(player, target, potion, ProgressionPlus.getPlugin())

//        val offline = meta.owningPlayer ?: return
//        val targetUUID = offline.uniqueId
//
//        val potion = activeGUIs.remove(player) ?: return
//        player.closeInventory()
//
//        // Handle clicking yourself (send self-request)
//        if (targetUUID == player.uniqueId) {
//            TeleportRequestManager.createRequest(
//                player,
//                player,
//                potion,
//                ProgressionPlus.getPlugin(),
//                teleportToSpawn = true
//            )
//            return
//        }
    }

}
