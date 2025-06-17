package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object TideCrystal : CustomItem("tide_crystal", Rarity.COMPONENT) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.PRISMARINE_CRYSTALS)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Tide Crystal")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("Hardened from the heart of the ocean."),
                ItemLore.separator(),
                ItemLore.lore("It hums with deepwater pressure."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "tide_crystal")

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Dropped by Elder Guardians.")
    }
}
