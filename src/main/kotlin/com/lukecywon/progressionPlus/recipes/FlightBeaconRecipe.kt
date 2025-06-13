package com.lukecywon.progressionPlus.recipes

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.FlightBeacon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe

class FlightBeaconRecipe : Recipe {
    val plugin = ProgressionPlus.getPlugin()!!

    override val nameSpacedKey: NamespacedKey = NamespacedKey(plugin, "flight_beacon_recipe")

    override fun getRecipe(): ShapedRecipe {

        val recipe = ShapedRecipe(NamespacedKey(plugin, "flight_beacon_recipe"), FlightBeacon.createItemStack())
        recipe.shape(
            "DDD",
            "DCD",
            "NNN"
        )
        recipe.setIngredient('D', Material.DIAMOND_BLOCK)
        recipe.setIngredient('N', Material.NETHERITE_BLOCK)
        recipe.setIngredient('C', Material.END_CRYSTAL) //replace with flight core later

        return recipe
    }
}
