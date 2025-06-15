package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.mechanics.CustomItemWithRecipe
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

object RecipeGenerator {
    fun generateRecipe(item: CustomItemWithRecipe): ShapedRecipe {
        val key = item.key
        val result = item.createItemStack()
        val recipe = ShapedRecipe(key, result)

        val layout = item.getRecipe()
        require(layout.size == 9) { "Recipe list must have 9 elements (3x3 grid)" }

        // Map unique non-null materials to characters A–I
        val materialToChar = mutableMapOf<Material, Char>()
        var nextChar = 'A'

        val grid = Array(3) { row ->
            CharArray(3) { col ->
                val index = row * 3 + col
                val mat = layout[index]
                if (mat != null) {
                    materialToChar.computeIfAbsent(mat) {
                        nextChar++
                        (nextChar - 1)
                    }
                } else {
                    ' '
                }
            }
        }

        recipe.shape(
            String(grid[0]),
            String(grid[1]),
            String(grid[2])
        )

        materialToChar.forEach { (mat, char) ->
            recipe.setIngredient(char, mat)
        }

        return recipe
    }
}
