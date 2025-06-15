package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.utility.common.SpeedBanner
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class SpeedBannerRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "speed_banner_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "speed_banner_recipe"), SpeedBanner.createItemStack())
        recipe.shape(
            "WWW",
            "WPW",
            " S "
        )
        recipe.setIngredient('W', Material.LIGHT_BLUE_WOOL)
        recipe.setIngredient('P', Material.SUGAR)
        recipe.setIngredient('S', Material.STICK)

        return recipe
    }
}
