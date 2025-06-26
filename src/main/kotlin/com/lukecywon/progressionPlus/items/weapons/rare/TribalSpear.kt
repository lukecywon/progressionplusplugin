package com.lukecywon.progressionPlus.items.weapons.rare

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
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object TribalSpear : CustomItem("tribal_spear", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.TRIDENT)
        item = applyBaseDamage(item, 5.0)
        item = applyBaseAttackSpeed(item, 0.3)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Tribal Spear")
                .color(NamedTextColor.DARK_GREEN)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Spear Throw", Activation.RIGHT_CLICK),
                ItemLore.description("Toss a sharp stick forward"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A weapon passed down through forgotten bloodlines."),
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "tribal_spear")

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.FLINT, null,
            null, Material.STICK, null,
            null, Material.STICK, null
        ))
    }
}