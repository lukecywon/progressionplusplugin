package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.utility.common.JumpBanner
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class JumpBannerRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "jump_banner_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "jump_banner_recipe"), JumpBanner.createItemStack())
        recipe.shape(
            "WWW",
            "WPW",
            " S "
        )
        recipe.setIngredient('W', Material.LIME_WOOL)
        recipe.setIngredient('P', Material.RABBIT_FOOT)
        recipe.setIngredient('S', Material.STICK)

        return recipe
    }
}
