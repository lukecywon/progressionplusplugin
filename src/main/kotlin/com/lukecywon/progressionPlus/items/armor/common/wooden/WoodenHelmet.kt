package com.lukecywon.progressionPlus.items.armor.common.wooden

import com.lukecywon.progressionPlus.utils.enums.Rarity
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
import org.bukkit.inventory.meta.SkullMeta

object WoodenHelmet : CustomItem("wooden_helmet", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = createCustomHead("http://textures.minecraft.net/texture/d4dd217ae569605ec023911168ae956412e1616ace2d6c464d42bc6caee0bdcf")
        val meta = item.itemMeta as SkullMeta

        meta.displayName(Component.text("Wooden Helmet", NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD))

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
                NamespacedKey(NamespacedKey.MINECRAFT, "helmet_armor"),
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