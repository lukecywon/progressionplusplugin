package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.AbyssalBox
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class AbyssalBoxListener : Listener {

    companion object {
        private const val BOX_SIZE = 9
        private val GUI_TITLE = Component.text("☯ Abyssal Box")
        private val boxStorage = mutableMapOf<String, Array<ItemStack?>>()
        private val openBoxes = mutableMapOf<Player, String>()
    }

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!AbyssalBox.isThisAbyssalBox(item)) return

        val boxId = AbyssalBox.getBoxId(item) ?: return

        event.isCancelled = true
        openAbyssalBox(player, boxId)
        player.playSound(player.location, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f)
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        val inv = event.inventory

        if (inv.size != BOX_SIZE || event.view.title() != GUI_TITLE) return

        val boxId = openBoxes.remove(player) ?: return
        boxStorage[boxId] = inv.contents
        player.playSound(player.location, Sound.BLOCK_ENDER_CHEST_CLOSE, 1f, 1f)
    }

    private fun openAbyssalBox(player: Player, boxId: String) {
        val inv: Inventory = Bukkit.createInventory(null, BOX_SIZE, GUI_TITLE)
        val contents = boxStorage[boxId]
        if (contents != null) inv.contents = contents

        player.openInventory(inv)
        openBoxes[player] = boxId
    }
}
