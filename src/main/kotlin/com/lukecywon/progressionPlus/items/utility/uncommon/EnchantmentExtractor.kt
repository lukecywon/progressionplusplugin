package com.lukecywon.progressionPlus.items.utility.uncommon

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
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

object EnchantmentExtractor : CustomItem("enchantment_extractor", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.QUARTZ)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Enchantment Extractor")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Extract Enchantments", Activation.OFFHAND),
                ItemLore.description("Destroys the item in your main hand and converts its enchantments into books."),
                ItemLore.separator(),
                ItemLore.lore("Preserve knowledge at the cost of material.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "enchantment_extractor")
        item.itemMeta = meta
        return applyMeta(item)
    }
    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.GLOWSTONE_DUST, Material.LAPIS_LAZULI, Material.GLOWSTONE_DUST,
            Material.BOOK, Material.PRISMARINE_CRYSTALS, Material.BOOK,
            Material.GLOWSTONE_DUST, Material.LAPIS_LAZULI, Material.GLOWSTONE_DUST
        ))
    }

}
