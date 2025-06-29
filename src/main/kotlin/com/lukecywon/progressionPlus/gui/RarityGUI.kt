package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object RarityGUI {
    private const val SIZE = 9 * 5
    private const val RAW_TITLE = "Recipe Book"
    private val TITLE = Component.text(RAW_TITLE)

    fun open(player: Player) {
        val gui = Bukkit.createInventory(null, SIZE, TITLE)

        val glassRows = listOf(
            0, 1, 2, 3, 5, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 41, 42, 43, 44
        )

        glassRows.forEach { index ->
            gui.setItem(index, ItemStack(Material.BLACK_STAINED_GLASS_PANE).apply {
                itemMeta = itemMeta?.apply {
                    displayName(Component.text(""))
                }
            })
        }

        gui.setItem(4, createTitleBook())

        // Rarity buttons
        gui.setItem(19, createConcrete(Material.WHITE_CONCRETE, Rarity.COMMON))
        gui.setItem(20, createConcrete(Material.LIME_CONCRETE, Rarity.UNCOMMON))
        gui.setItem(21, createConcrete(Material.LIGHT_BLUE_CONCRETE, Rarity.RARE))
        gui.setItem(22, createConcrete(Material.PURPLE_CONCRETE, Rarity.EPIC))
        gui.setItem(23, createConcrete(Material.YELLOW_CONCRETE, Rarity.LEGENDARY))
        gui.setItem(24, createConcrete(Material.GRAY_CONCRETE, Rarity.COMPONENT))
        gui.setItem(25, createConcrete(Material.CYAN_CONCRETE, Rarity.PROGRESSION))

        gui.setItem(40, ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Close").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
            }
        })

        player.openInventory(gui)
    }

    private fun createConcrete(material: Material, rarity: Rarity): ItemStack {
        return ItemStack(material).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text(rarity.displayName).color(rarity.color).decoration(TextDecoration.ITALIC, false))
            }
        }
    }

    fun createTitleBook(): ItemStack {
        val item = ItemStack(Material.BOOK)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Recipe Book")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false)
        )

        meta.lore(
            listOf(
                Component.text("As you progress through your adventure, you will").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                Component.text("gain access to various new recipes.").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
            )
        )

        meta.addEnchant(Enchantment.UNBREAKING, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        item.itemMeta = meta
        return item
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != RAW_TITLE) return

        e.isCancelled = true
        val clicked = e.currentItem ?: return

        when (clicked.type) {
            Material.WHITE_CONCRETE       -> ItemListGUI.open(player, Rarity.COMMON)
            Material.LIME_CONCRETE        -> ItemListGUI.open(player, Rarity.UNCOMMON)
            Material.LIGHT_BLUE_CONCRETE  -> ItemListGUI.open(player, Rarity.RARE)
            Material.PURPLE_CONCRETE      -> ItemListGUI.open(player, Rarity.EPIC)
            Material.YELLOW_CONCRETE      -> ItemListGUI.open(player, Rarity.LEGENDARY)
            Material.GRAY_CONCRETE        -> ItemListGUI.open(player, Rarity.COMPONENT)
            Material.CYAN_CONCRETE        -> ItemListGUI.open(player, Rarity.PROGRESSION)
            Material.BARRIER              -> player.closeInventory()
            else                          -> return
        }

        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
    }
}
