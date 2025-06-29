package com.lukecywon.progressionPlus.utils.dataclasses

import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe

data class StoredRecipe(val nameSpacedKey: NamespacedKey, val recipe: Recipe)
