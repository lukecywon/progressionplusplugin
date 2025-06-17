package com.lukecywon.progressionPlus.items.component

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

object EnderiteIngot : CustomItem("enderite_ingot", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_INGOT)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Enderite Ingot")
                .color(NamedTextColor.DARK_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("A rare ingot forged from exotic materials."),
                ItemLore.separator(),
                ItemLore.lore("It pulses faintly, as if remembering distant stars."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "enderite_ingot")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.END_STONE, Material.END_STONE, Material.END_STONE,
            Material.END_STONE, Material.NETHERITE_INGOT, Material.END_STONE,
            Material.END_STONE, Material.END_STONE, Material.END_STONE
        ))
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Found in Ancient City chests.")
    }
}