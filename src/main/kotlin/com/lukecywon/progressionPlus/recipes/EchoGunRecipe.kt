package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.EchoGun
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class EchoGunRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!
    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "beam_gun_recipe")

    override fun getRecipe(): ShapedRecipe {
        val recipe = ShapedRecipe(NamespacedKey(plugin, "beam_gun_recipe"), EchoGun.createItemStack())
        recipe.shape(" SS", "NES", "K  ")

        recipe.setIngredient('S', Material.SCULK_CATALYST)
        recipe.setIngredient('E', Material.ECHO_SHARD)
        recipe.setIngredient('N', Material.NETHER_STAR)
        recipe.setIngredient('K', Material.STICK)

        return recipe
    }
}