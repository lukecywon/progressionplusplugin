package com.lukecywon.progressionPlus.items.armor.common

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object WoodenChestplate : CustomItem("wooden_chestplate", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.LEATHER_CHESTPLATE)
        val meta = item.itemMeta

        meta.displayName(Component.text("Wooden Chestplate", Rarity.COMMON.color))

        meta.removeAttributeModifier(Attribute.ARMOR)
        meta.addAttributeModifier(
            Attribute.ARMOR,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "chestplate_armor"),
                4.0,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.OAK_WOOD, null, Material.OAK_WOOD,
            Material.OAK_WOOD, Material.OAK_WOOD, Material.OAK_WOOD,
            Material.OAK_WOOD, Material.OAK_WOOD, Material.OAK_WOOD
        ))
    }
}