package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

class SetupItems {
    @RunOnEnable
    fun items(plugin: JavaPlugin) {
        val items = Reflections("com.lukecywon.progressionPlus.items")
        val classes = items.getSubTypesOf(CustomItem::class.java).sortedWith(compareBy({ it.name.substringBeforeLast('.') }, { it.simpleName }))

        // Initialize all CustomItem objects
        classes.forEach { customItem ->
            try {
                // Use object instance of CustomItem and get their recipes
                val itemObject = customItem.kotlin.objectInstance!!

                if (RecipeGenerator.generateRecipe(itemObject) == null) return@forEach

                val recipe = RecipeGenerator.generateRecipe(itemObject)
                val key = itemObject.key

                if (Bukkit.getRecipe(key) != null) {
                    Bukkit.removeRecipe(key)
                }

                Bukkit.addRecipe(recipe)
            } catch (e: Exception) {
                plugin.logger.warning("Failed to load item: ${customItem.simpleName}: ${e.message}")
            }
        }
    }
}