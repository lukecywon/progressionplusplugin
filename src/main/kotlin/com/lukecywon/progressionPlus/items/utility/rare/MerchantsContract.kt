package com.lukecywon.progressionPlus.items.utility.rare

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

object MerchantsContract : CustomItem("merchants_contract", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Merchant's Contract")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Trade with Players", Activation.RIGHT_CLICK),
                ItemLore.description("Open a menu to request trades with others online."),
                ItemLore.separator(),
                ItemLore.lore("Signed in ink, bound by trust.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "merchants_contract")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.PAPER, Material.VILLAGER_SPAWN_EGG, Material.PAPER,
            Material.INK_SAC, Material.WRITABLE_BOOK, Material.INK_SAC,
            Material.PAPER, Material.EMERALD_BLOCK, Material.PAPER
        ))
    }
}
