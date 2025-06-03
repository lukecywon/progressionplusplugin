package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.RegenBanner
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class RegenBannerRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "regen_banner_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "regen_banner_recipe"), RegenBanner.createItemStack())
        recipe.shape(
            "WWW",
            "WPW",
            " S "
        )
        recipe.setIngredient('W', Material.PINK_WOOL)
        recipe.setIngredient('P', Material.GHAST_TEAR)
        recipe.setIngredient('S', Material.STICK)

        return recipe
    }
}
