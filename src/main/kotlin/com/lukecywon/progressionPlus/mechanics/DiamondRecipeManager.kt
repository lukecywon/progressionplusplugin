package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.recipes.StoredRecipe
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object DiamondRecipeManager : Manager {
    private val recipes = mutableListOf<StoredRecipe>().apply {
        val recipeNameSpacedKeys = listOf(
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

        recipeNameSpacedKeys.forEach { nsk ->
            val recipe = Bukkit.getRecipe(nsk) ?: return@forEach
            val storedRecipe = StoredRecipe(nsk, recipe)
            add(storedRecipe)
        }
    }

    override fun start(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                val diamondUnlocked = plugin.config.getBoolean("diamond-unlocked")

                if (diamondUnlocked) {
                    addAllToBukkit()
                } else {
                    if (!diamondRecipesPresent()) return
                    removeAllFromBukkit()
                }
            }
        }.runTaskTimer(plugin, 20L, 0L)
    }

    private fun removeAllFromBukkit() {
        recipes.forEach { storedRecipe ->
            Bukkit.removeRecipe(storedRecipe.nameSpacedKey)
        }
    }

    private fun addAllToBukkit() {
        recipes.forEach { storedRecipe ->
            Bukkit.addRecipe(storedRecipe.recipe)
        }
    }

    private fun diamondRecipesPresent() : Boolean {
        return Bukkit.getRecipe(recipes.first().nameSpacedKey) != null
    }
}