package com.lukecywon.progressionPlus.items.armor.rare.paladin

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object PaladinHelmet : CustomItem("paladin_helmet", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = createCustomHead("http://textures.minecraft.net/texture/60fd2cc9116c0f724bdcdbf2e9633cb0a7d453f8b3a1ad9d1493e5e6f1281555")
        val meta = item.itemMeta

        meta.displayName(Component.text("Paladin Helmet", NamedTextColor.GOLD).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.description("A basic helmet crafted straight from nature."),
                ItemLore.separator()
            )
        )

        meta.removeAttributeModifier(Attribute.ARMOR)
        meta.addAttributeModifier(
            Attribute.ARMOR,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "armor"),
                1.5,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.OAK_WOOD, Material.OAK_WOOD, Material.OAK_WOOD,
            Material.OAK_WOOD, null, Material.OAK_WOOD,
            null,null,null
        ))
    }
}