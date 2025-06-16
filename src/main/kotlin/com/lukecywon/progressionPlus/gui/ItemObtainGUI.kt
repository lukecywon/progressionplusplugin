package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.gui.GUI
import com.lukecywon.progressionPlus.items.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ItemObtainGUI : GUI("Item Info", 9 * 5) {
    private var rarity: Rarity = Rarity.COMMON // Used for back button navigation

    override fun open(player: Player) {}

    fun open(player: Player, item: CustomItem) {
        rarity = item.getRarity()
        val gui: Inventory = Bukkit.createInventory(null, getSize(), getTitleComponent())

        val glassRows = listOf(
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 42, 43, 44
        )

        val greenGlassRows = listOf(
            21, 22, 23
        )

        // Green Glass
        greenGlassRows.forEach {
            gui.setItem(it, ItemStack(Material.GREEN_STAINED_GLASS_PANE).apply {
                itemMeta = itemMeta.apply {
                    displayName(Component.text(""))
                }
            })
        }

        glassRows.forEach {
            gui.setItem(it, ItemStack(Material.BLACK_STAINED_GLASS_PANE).apply {
                itemMeta = itemMeta.apply {
                    displayName(Component.text(""))
                }
            })
        }

        // Center: Oak Sign showing how to obtain
        val infoSign = ItemStack(Material.OAK_SIGN).apply {
            itemMeta = itemMeta?.apply {
                val lore = item.getExtraInfo()
                displayName(Component.text("How to Obtain").color(NamedTextColor.GOLD).decoration(TextDecoration.BOLD, false))
                if (lore.isNotEmpty()) {
                    lore(lore.map { Component.text(it).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false) })
                } else {
                    lore(listOf(Component.text("No info available").color(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, true)))
                }
            }
        }
        gui.setItem(20, infoSign)

        // Result item
        gui.setItem(24, item.createItemStack())

        // Back button
        val back = ItemStack(Material.ARROW).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Back").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            }
        }
        gui.setItem(40, back)

        // Close button
        val close = ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Close").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
            }
        }
        gui.setItem(41, close)

        player.openInventory(gui)
    }

    override fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != getTitle()) return
        e.isCancelled = true

        val clicked = e.currentItem ?: return
        if (clicked.type == Material.BARRIER) {
            player.closeInventory()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        } else if (clicked.type == Material.ARROW) {
            ItemListGUI.open(player, rarity)
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        }
    }
}
