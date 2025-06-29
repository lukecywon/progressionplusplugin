package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object EnderChestRecipe : Recipe {
    override fun register() {
        val key = Material.ENDER_CHEST.key
        Bukkit.removeRecipe(key)

        val newKey = NamespacedKey(ProgressionPlus.getPlugin(), "ender_chest")
        val result = ItemStack(Material.ENDER_CHEST)

        if (Bukkit.getRecipe(key) != null) {
            Bukkit.removeRecipe(key)
        }

        val recipe = ShapedRecipe(key, result)

        recipe.shape(
            "OOO",
            "OEO",
            "OOO"
        )

        recipe.setIngredient('O', Material.OBSIDIAN)
        recipe.setIngredient('E', Material.ENDER_PEARL)

        Bukkit.addRecipe(recipe)
    }
}