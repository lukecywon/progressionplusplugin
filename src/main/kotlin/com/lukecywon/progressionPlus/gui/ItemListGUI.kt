package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItemRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object ItemListGUI {
    private const val ROWS = 3
    private const val SIZE = ROWS * 9
    private const val titlePrefix = "📦 Items: "

    fun open(player: Player, rarity: Rarity) {
        val title = Component.text("$titlePrefix${rarity.displayName}")
        val gui = Bukkit.createInventory(null, SIZE, title)

        val items = CustomItemRegistry.getAll().filter { it.getRarity() == rarity }

        for ((i, item) in items.withIndex()) {
            if (i >= SIZE - 1) break
            gui.setItem(i, item.createItemStack())
        }

        gui.setItem(26, ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Back").color(NamedTextColor.RED))
            }
        })

        player.openInventory(gui)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title // returns a String (yes, deprecated)
        if (!title.startsWith(titlePrefix)) return

        e.isCancelled = true
        val clicked = e.currentItem ?: return

        if (clicked.type == Material.BARRIER) {
            RarityGUI.open(player)
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
            return
        }

        val match = CustomItemRegistry.getAll().find { it.isThisItem(clicked) }
        if (match != null) {
            ItemDetailGUI.open(player, match)
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        }
    }
}

