package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object RefinedEye : CustomItem("custom_ender_eye", Rarity.PROGRESSION) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENDER_EYE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Refined Eye")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.description("A crystallized version of the Ender Eye,"),
                ItemLore.description("forged through immense magical compression."),
                ItemLore.separator(),
                ItemLore.lore("“It never breaks.”")
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<Material?> {
        return listOf(
            Material.ENDER_PEARL, Material.NETHERITE_SCRAP, Material.ENDER_PEARL,
            Material.BLAZE_POWDER, Material.NETHER_STAR, Material.BLAZE_POWDER,
            Material.ENDER_PEARL, Material.NETHERITE_SCRAP, Material.ENDER_PEARL
        )
    }
}
