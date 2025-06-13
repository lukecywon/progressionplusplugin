package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.persistence.PersistentDataType

object ItemEncyclopedia : CustomItem("item_encyclopedia", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta as BookMeta

        meta.title = "Item Encyclopedia"
        meta.author = "Progression Archives"
        meta.displayName(
            Component.text("Item Encyclopedia")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.description("Right-click to view all custom items."),
                ItemLore.description("Browse recipes, lore, and usage info."),
                ItemLore.separator(),
                ItemLore.lore("A record of forged legends and crafted relics.")
            )
        )

        meta.setCustomModelData(9001)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isItemEncyclopedia(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.WRITTEN_BOOK) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
