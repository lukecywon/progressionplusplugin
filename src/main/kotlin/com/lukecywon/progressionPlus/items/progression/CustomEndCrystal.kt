package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object CustomEndCrystal : CustomItem("custom_end_crystal", Rarity.PROGRESSION) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.END_CRYSTAL)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("End Crystal")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.description("A bound shard of raw energy."),
                ItemLore.separator(),
                ItemLore.lore("“Its pulse hums with caged divinity,”"),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.MaterialChoice(Material.GLASS),
            RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.ExactChoice(RefinedEye.createItemStack()), RecipeChoice.MaterialChoice(Material.GLASS),
            RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.MaterialChoice(Material.GHAST_TEAR), RecipeChoice.MaterialChoice(Material.GLASS)
        )
    }
}
