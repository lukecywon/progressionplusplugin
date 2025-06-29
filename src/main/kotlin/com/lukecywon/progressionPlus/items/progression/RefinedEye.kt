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
                ItemLore.description("Used to find and open the end portal"),
                ItemLore.separator(),
                ItemLore.lore("“Unshattered by fate, guided by purpose.”")
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.ENDER_PEARL, Material.NETHERITE_SCRAP, Material.ENDER_PEARL,
            Material.BLAZE_POWDER, Material.NETHER_STAR, Material.BLAZE_POWDER,
            Material.ENDER_PEARL, Material.NETHERITE_SCRAP, Material.ENDER_PEARL
        ))
    }
}
