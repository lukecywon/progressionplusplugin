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

object AetherCore : CustomItem("aether_core", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.HEART_OF_THE_SEA)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Aether Core")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("An ancient core pulsating with skybound energy."),
                ItemLore.separator(),
                ItemLore.lore("Its presence lifts the world ever so slightly.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "aether_core")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.ELYTRA, Material.ELYTRA, Material.ELYTRA,
            Material.ELYTRA, Material.NETHER_STAR, Material.ELYTRA,
            Material.ELYTRA, Material.ELYTRA, Material.ELYTRA
        ))
    }
}
