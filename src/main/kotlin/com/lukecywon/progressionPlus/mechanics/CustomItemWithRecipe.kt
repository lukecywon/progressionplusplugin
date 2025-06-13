package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.Material

abstract class CustomItemWithRecipe(name: String, rarity: Rarity) : CustomItem(name, rarity) {
    abstract fun getRecipe(): List<Material?> // 9 items, left to right, top to bottom
}
