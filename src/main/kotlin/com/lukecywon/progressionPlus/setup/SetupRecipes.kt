package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.annotations.RunOnEnable
import com.lukecywon.progressionPlus.recipes.Recipe
import org.reflections.Reflections

class SetupRecipes {
    @RunOnEnable
    private fun recipes() {
        // Override vanilla recipes
        val recipes = Reflections("com.lukecywon.progressionPlus.recipes")
        val classes = recipes.getSubTypesOf(Recipe::class.java)

        classes.forEach { recipe ->
            recipe.getDeclaredConstructor().newInstance().register()
        }
    }
}