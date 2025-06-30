package com.lukecywon.progressionPlus.items.utility.common

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object AbsorptionBanner : CustomItem("absorption_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Absorption Banner")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Absorption", Activation.RIGHT_CLICK),
                ItemLore.description("Grant Absorption to nearby players for 30s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("A radiant shield for allies."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "absorption_banner")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.ORANGE_WOOL, Material.ORANGE_WOOL, Material.ORANGE_WOOL,
            Material.ORANGE_WOOL, Material.GOLDEN_APPLE, Material.ORANGE_WOOL,
            null, Material.STICK, null
        ))
    }
}