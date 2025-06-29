package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItemRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

object ItemListGUI {
    private const val SIZE = 5 * 9
    private const val titlePrefix = "Items: "

    fun open(player: Player, rarity: Rarity) {
        val title = Component.text("$titlePrefix${rarity.displayName}")
        val gui = Bukkit.createInventory(null, SIZE, title)

        val items = CustomItem.getAll().filter { it.getRarity() == rarity }

        val glassRows = listOf(
            0, 1, 2, 3, 4, 5, 6, 7, 8,
            9, 17,
            18, 26,
            27, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
        )
        val availableSlots = (0 until SIZE).filter { it !in glassRows }.toList()

        // Blank Spots
        glassRows.forEach { no ->
            if (no == 40 || no == 41) return@forEach
            gui.setItem(no, ItemStack(Material.BLACK_STAINED_GLASS_PANE).apply {
                itemMeta = itemMeta.apply {
                    displayName(Component.text(""))
                }
            })
        }

        // Fill only allowed slots with items
        for ((index, item) in items.withIndex()) {
            if (index >= availableSlots.size) break
            gui.setItem(availableSlots[index], item.createItemStack())
        }

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

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title // returns a String (yes, deprecated)
        if (!title.startsWith(titlePrefix)) return

        e.isCancelled = true
        val clicked = e.currentItem ?: return

        if (clicked.type == Material.ARROW) {
            RarityGUI.open(player)
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
            return
        } else if (clicked.type == Material.BARRIER) {
            player.closeInventory()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        }

        val match = CustomItem.getAll().find { it.isThisItem(clicked) }
        if (match != null) {
            if (match.hasRecipe()) {
                ItemRecipeGUI.open(player, match)
            } else {
                ItemObtainGUI.open(player, match)
            }
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        }
    }
}

