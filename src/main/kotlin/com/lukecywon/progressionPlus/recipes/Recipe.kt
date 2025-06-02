package com.lukecywon.progressionPlus.recipes

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

interface Recipe {
    val nameSpacedKey: NamespacedKey
    fun getRecipe(): ShapedRecipe
}