package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object CustomEnderChest : CustomItem("custom_ender_chest", Rarity.PROGRESSION, true) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENDER_CHEST)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Ender Chest")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.description("Crafted to resist corruption."),
                ItemLore.separator(),
                ItemLore.lore("“Its contents rest between realms.“"),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN,
            Material.OBSIDIAN, Material.ENDER_PEARL, Material.OBSIDIAN,
            Material.OBSIDIAN, Material.OBSIDIAN, Material.OBSIDIAN
        ))
    }
}
