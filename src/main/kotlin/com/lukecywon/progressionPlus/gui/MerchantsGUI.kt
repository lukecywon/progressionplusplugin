package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.mechanics.MerchantsRequestManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object MerchantsGUI {
    fun openPlayerSelectGUI(requester: Player) {
        val targets = Bukkit.getOnlinePlayers().filter { it.uniqueId != requester.uniqueId }
        if (targets.isEmpty()) {
            requester.sendMessage("§cNo other players online.")
            requester.playSound(requester.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
            return
        }

        val size = ((targets.size - 1) / 9 + 1) * 9
        val gui = Bukkit.createInventory(null, size, "Select Trade Partner")

        for (target in targets) {
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            meta.owningPlayer = target
            meta.displayName(Component.text("§b${target.name}"))
            head.itemMeta = meta
            gui.addItem(head)
        }

        val cancel = ItemStack(Material.BARRIER)
        val cancelMeta = cancel.itemMeta
        cancelMeta.displayName(Component.text("§cCancel"))
        cancel.itemMeta = cancelMeta
        gui.setItem(gui.size - 5, cancel)

        requester.openInventory(gui)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != "Select Trade Partner") return
        e.isCancelled = true

        val clicked = e.currentItem ?: return
        if (clicked.type == Material.BARRIER) {
            player.closeInventory()
            return
        }

        val meta = clicked.itemMeta as? SkullMeta ?: return
        val target = meta.owningPlayer ?: return

        player.closeInventory()
        MerchantsRequestManager.requestTrade(player, target)
    }
}
