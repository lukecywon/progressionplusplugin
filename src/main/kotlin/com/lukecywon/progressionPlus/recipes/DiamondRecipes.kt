package com.lukecywon.progressionPlus.recipes

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey

object DiamondRecipes {
    private val recipes = mutableListOf<StoredRecipe>()

    fun storeAll() {
        val diamondRecipes = listOf(
            NamespacedKey.minecraft("diamond_sword"),
            NamespacedKey.minecraft("diamond_pickaxe"),
            NamespacedKey.minecraft("diamond_axe"),
            NamespacedKey.minecraft("diamond_shovel"),
            NamespacedKey.minecraft("diamond_hoe"),
            NamespacedKey.minecraft("diamond_helmet"),
            NamespacedKey.minecraft("diamond_chestplate"),
            NamespacedKey.minecraft("diamond_leggings"),
            NamespacedKey.minecraft("diamond_boots")
        )

        diamondRecipes.forEach { item ->
            val recipe = Bukkit.getRecipe(item) ?: return@forEach
            val storedRecipe = StoredRecipe(item, recipe)
            recipes.add(storedRecipe)
        }
    }
}




