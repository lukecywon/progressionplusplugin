package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.AbsorptionBanner
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class AbsorptionBannerRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "absorption_banner_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "absorption_banner_recipe"), AbsorptionBanner.createItemStack())
        recipe.shape(
            "WWW",
            "WPW",
            " S "
        )
        recipe.setIngredient('W', Material.ORANGE_WOOL)
        recipe.setIngredient('P', Material.GOLDEN_APPLE)
        recipe.setIngredient('S', Material.STICK)

        return recipe
    }
}
