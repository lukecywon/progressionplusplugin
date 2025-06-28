package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object HealthCrystal : CustomItem("health_crystal", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Health Crystal")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Soul Fusion", Activation.CONSUME),
                ItemLore.description("Permanently increases max health by 1 heart"),
                ItemLore.description("Cannot be used when less than 10 hearts"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A shard of life itself, yearning to become one with its bearer."),
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "health_crystal")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.DIAMOND_BLOCK, null, Material.DIAMOND_BLOCK,
            Material.GLISTERING_MELON_SLICE, Material.ENCHANTED_GOLDEN_APPLE, Material.GLISTERING_MELON_SLICE,
            null, Material.EMERALD_BLOCK, null
        ))
    }
}
