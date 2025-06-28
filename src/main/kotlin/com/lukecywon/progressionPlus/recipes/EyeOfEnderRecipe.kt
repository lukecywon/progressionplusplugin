package com.lukecywon.progressionPlus.recipes

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey

object EyeOfEnderRecipe : Recipe {
    override fun register() {
        // Remove the vanilla Eye of Ender recipe
        Bukkit.removeRecipe(NamespacedKey.minecraft("ender_eye"))
    }
}
