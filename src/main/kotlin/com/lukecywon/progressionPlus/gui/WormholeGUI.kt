package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.mechanics.TeleportRequestManager
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object WormholeGUI {
    private val activeGUIs = mutableMapOf<Player, ItemStack>() // player → potion

    fun open(requester: Player, potionItem: ItemStack): Boolean {
        val players = Bukkit.getOnlinePlayers().filter { it.uniqueId != requester.uniqueId }

        if (players.isEmpty()) {
            requester.sendMessage("§cNo other players online. The potion was returned.")
            requester.playSound(requester.location, Sound.ENTITY_VILLAGER_NO, 0.6f, 1.0f)
            refundPotion(requester, potionItem)
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

        inventory.setItem(inventory.size - 5, cancel) // Center last row
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

            activeGUIs.remove(player)?.let { refundPotion(player, it) }
            return
        }

        val meta = clicked.itemMeta as? SkullMeta ?: return
        val offline = meta.owningPlayer ?: return
        val target = Bukkit.getPlayer(offline.uniqueId) ?: return

        val potion = activeGUIs.remove(player) ?: return
        player.closeInventory()

        if (TeleportRequestManager.hasRequestFor(target.uniqueId)) {
            player.sendMessage("§cThat player already has a pending teleport request.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6f, 0.5f)
            refundPotion(player, potion)
            return
        }

        if (TeleportRequestManager.hasOutgoingRequestFrom(player.uniqueId)) {
            player.sendMessage("§cYou already have a pending teleport request.")
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 0.6f, 0.5f)
            refundPotion(player, potion)
            return
        }

        // Success
        TeleportRequestManager.createRequest(player, target, potion, ProgressionPlus.getPlugin())
    }

    private fun refundPotion(player: Player, potion: ItemStack) {
        // Add potion back
        val leftover = player.inventory.addItem(potion)
        if (leftover.isNotEmpty()) {
            leftover.values.forEach { item -> player.world.dropItemNaturally(player.location, item) }
        }

        // Remove 1 glass bottle (if any)
        val glass = player.inventory.contents.firstOrNull { it?.type == Material.GLASS_BOTTLE }
        glass?.let {
            it.amount -= 1
            if (it.amount <= 0) player.inventory.remove(it)
        }
    }
}
