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
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object LuckTalisman : CustomItem("luck_talisman", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.EMERALD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Luck Talisman")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Fortune's Blessing", Activation.RIGHT_CLICK),
                ItemLore.description("Grants Luck IV for 15 seconds"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("A charm whispered with fate, tilting odds in your favor."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.CHAIN, null, Material.CHAIN,
            Material.CHAIN, Material.RABBIT_FOOT, Material.CHAIN,
            null, Material.EMERALD, null
        ))
    }
}
