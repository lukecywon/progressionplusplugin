package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object InfernalShard : CustomItem("infernal_shard", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Infernal Shard")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("A searing shard imbued with nether heat."),
                ItemLore.separator(),
                ItemLore.lore("It radiates a hunger for ignition.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "infernal_shard")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.BLAZE_ROD, Material.WITHER_ROSE, Material.BLAZE_ROD,
            Material.NETHER_WART, Material.NETHERITE_SCRAP, Material.NETHER_WART,
            Material.BLAZE_ROD, Material.WITHER_ROSE, Material.BLAZE_ROD
        ))
    }
}
