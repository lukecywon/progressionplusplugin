package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object SteelIngot : CustomItem("steel_ingot", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_INGOT)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Steel Ingot")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("A refined alloy forged from iron and carbon, valued for its strength and durability."),
                ItemLore.separator(),
                ItemLore.lore("Its surface glints with the memory of countless forges."),
                ItemLore.lore("Heavier than iron, yet shaped by fire and will.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "steel_ingot")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.CHARCOAL, null,
            Material.CHARCOAL, Material.IRON_INGOT, Material.CHARCOAL,
            null, Material.CHARCOAL, null
        ))
    }
}