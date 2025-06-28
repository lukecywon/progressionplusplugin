package com.lukecywon.progressionPlus.listeners.utility.epic

import com.lukecywon.progressionPlus.items.utility.epic.AbyssalBox
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.File

class AbyssalBoxListener : Listener {

    companion object {
        private const val BOX_SIZE = 9
        private val GUI_TITLE = Component.text("☯ Abyssal Box")
        private val boxStorage = mutableMapOf<String, Array<ItemStack?>>()
        private val openBoxes = mutableMapOf<Player, String>()
        private val storageDir = File(Bukkit.getPluginsFolder(), "progressionPlus/abyssal_boxes")

        init {
            if (!storageDir.exists()) storageDir.mkdirs()
        }

        private fun loadBox(boxId: String): Array<ItemStack?> {
            val file = File(storageDir, "$boxId.yml")
            if (!file.exists()) return Array(BOX_SIZE) { null }

            val config = YamlConfiguration.loadConfiguration(file)
            return Array(BOX_SIZE) { index ->
                config.getItemStack("slot.$index")
            }
        }

        private fun saveBox(boxId: String, contents: Array<ItemStack?>) {
            val file = File(storageDir, "$boxId.yml")
            val config = YamlConfiguration()

            contents.forEachIndexed { i, item ->
                if (item != null) {
                    config.set("slot.$i", item)
                }
            }

            config.save(file)
        }

        private fun openAbyssalBox(player: Player, boxId: String) {
            val inv: Inventory = Bukkit.createInventory(null, BOX_SIZE, GUI_TITLE)
            val contents = boxStorage.computeIfAbsent(boxId) { loadBox(it) }
            inv.contents = contents
            player.openInventory(inv)
            openBoxes[player] = boxId
        }
    }

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (!AbyssalBox.isThisItem(item)) return

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

        var removedCount = 0

        val filteredContents = inv.contents.mapIndexed { index, item ->
            if (AbyssalBox.isThisItem(item)) {
                inv.setItem(index, null) // Remove from GUI as well
                removedCount++
                null
            } else item
        }.toTypedArray()

        boxStorage[boxId] = filteredContents
        saveBox(boxId, filteredContents)

        player.playSound(player.location, Sound.BLOCK_ENDER_CHEST_CLOSE, 1f, 1f)

        if (removedCount > 0) {
            player.sendMessage("§cYou cannot store Abyssal Boxes inside themselves. Removed $removedCount item(s).")
        }
    }

    @EventHandler
    fun onBoxInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val clickedInv = event.clickedInventory ?: return
        val viewTitle = event.view.title()
        if (viewTitle != GUI_TITLE || event.slotType == InventoryType.SlotType.OUTSIDE) return

        val item = event.cursor ?: return
        val shiftItem = event.currentItem

        // Case 1: Placing with cursor
        if (AbyssalBox.isThisItem(item)) {
            event.isCancelled = true
            player.setItemOnCursor(item) // return item to cursor
            player.sendMessage("§cYou cannot store Abyssal Boxes inside other Abyssal Boxes.")
            player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.9f)
            return
        }

        // Case 2: Shift-clicking an Abyssal Box in
        if (event.click == ClickType.SHIFT_LEFT || event.click == ClickType.SHIFT_RIGHT) {
            if (AbyssalBox.isThisItem(shiftItem)) {
                event.isCancelled = true
                player.sendMessage("§cYou cannot store Abyssal Boxes inside other Abyssal Boxes.")
                player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1f, 0.9f)
            }
        }
    }

}
