package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.CustomItemWithRecipe
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object ItemDetailGUI {
    private const val SIZE = 9 * 5
    private const val RAW_TITLE = "🔍 Item Details"
    private val TITLE_COMPONENT = Component.text(RAW_TITLE)

    fun open(player: Player, item: CustomItem) {
        val gui: Inventory = Bukkit.createInventory(null, SIZE, TITLE_COMPONENT)

        // Recipe display
        val recipe = (item as? CustomItemWithRecipe)?.getRecipe() ?: List(9) { null }
        val grid = listOf(10, 11, 12, 19, 20, 21, 28, 29, 30)

        for (i in recipe.indices) {
            recipe[i]?.let { mat -> gui.setItem(grid[i], ItemStack(mat)) }
        }

        // Result item
        gui.setItem(24, item.createItemStack())

        // About this item (custom info only)
        val extraInfo = item.getExtraInfo()
        val loreSign = ItemStack(Material.OAK_SIGN).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("About this item").color(NamedTextColor.GOLD))
                if (extraInfo.isNotEmpty()) {
                    lore = extraInfo
                }
            }
        }
        gui.setItem(31, loreSign)

        // Back button
        val back = ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Back").color(NamedTextColor.RED))
            }
        }
        gui.setItem(44, back)

        player.openInventory(gui)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != RAW_TITLE) return
        e.isCancelled = true

        val clicked = e.currentItem ?: return
        if (clicked.type == Material.BARRIER) {
            player.closeInventory()
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
        }
    }
}
