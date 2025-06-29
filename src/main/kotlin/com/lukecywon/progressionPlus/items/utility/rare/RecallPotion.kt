package com.lukecywon.progressionPlus.items.utility.rare

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
import org.bukkit.persistence.PersistentDataType

object RecallPotion : CustomItem("recall_potion", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.POTION)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Recall Potion")
                .color(NamedTextColor.AQUA)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Recall", Activation.CONSUME),
                ItemLore.description("Drink to teleport to your spawn after 5s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("No matter how far, its magic finds the way home."),
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "recall_potion")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.ENDER_PEARL, null,
            Material.ENDER_PEARL, Material.GLASS_BOTTLE, Material.ENDER_PEARL,
            null, Material.ENDER_PEARL, null
        ))
    }
}
