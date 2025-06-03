package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.HasteBanner
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class HasteBannerRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "haste_banner_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "haste_banner_recipe"), HasteBanner.createItemStack())
        recipe.shape(
            "WWW",
            "WPW",
            " S "
        )
        recipe.setIngredient('W', Material.YELLOW_WOOL)
        recipe.setIngredient('P', Material.GOLDEN_PICKAXE)
        recipe.setIngredient('S', Material.STICK)

        return recipe
    }
}
