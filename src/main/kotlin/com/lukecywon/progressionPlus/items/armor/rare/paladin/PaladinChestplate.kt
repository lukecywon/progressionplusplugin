package com.lukecywon.progressionPlus.items.armor.rare.paladin

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object PaladinChestplate : CustomItem("paladin_chestplate", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.IRON_CHESTPLATE)
        item = applyArmor(item, 5.0)

        val meta = item.itemMeta

        meta.displayName(Component.text("Paladin Chestplate", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.description("A basic chestplate crafted straight from nature."),
                ItemLore.separator(),
                ItemLore.stats(item)
            )
        )

        // Custom trim for wood armor
        if (meta is ArmorMeta) {
            val material = TrimMaterial.GOLD
            val pattern = TrimPattern.DUNE
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

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