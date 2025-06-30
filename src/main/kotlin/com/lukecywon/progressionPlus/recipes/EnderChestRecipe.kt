package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

object EnderChestRecipe : Recipe {
    override fun register() {
        Bukkit.removeRecipe(NamespacedKey.minecraft("ender_chest"))
    }
}