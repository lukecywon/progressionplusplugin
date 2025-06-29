package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.AetherCore
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object FlightBeacon : CustomItem("flight_beacon", Rarity.EPIC) {
    val beaconKey = NamespacedKey(ProgressionPlus.getPlugin(), "flight_beacon")

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BEACON)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Flight Beacon")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Flight Zone", Activation.PASSIVE),
                ItemLore.description("Create a flying zone that uses netherite ingots as fuel"),
                ItemLore.description("Range scales with beacon base (only diamond blocks)"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("Built from brilliance, it bends the sky to your will."),
            )
        )

        meta.persistentDataContainer.set(beaconKey, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK), RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK), RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK),
            RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK), RecipeChoice.ExactChoice(AetherCore.createItemStack()), RecipeChoice.MaterialChoice(Material.DIAMOND_BLOCK),
            RecipeChoice.MaterialChoice(Material.NETHERITE_BLOCK), RecipeChoice.MaterialChoice(Material.NETHERITE_BLOCK), RecipeChoice.MaterialChoice(Material.NETHERITE_BLOCK)
        )
    }
}