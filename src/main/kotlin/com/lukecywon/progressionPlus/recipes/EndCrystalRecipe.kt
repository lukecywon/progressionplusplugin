package com.lukecywon.progressionPlus.recipes

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey

object EndCrystalRecipe : Recipe {
    override fun register() {
        Bukkit.removeRecipe(NamespacedKey.minecraft("end_crystal"))
    }
}