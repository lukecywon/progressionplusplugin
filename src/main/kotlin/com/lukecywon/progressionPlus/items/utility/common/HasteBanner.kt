package com.lukecywon.progressionPlus.items.utility.common

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

object HasteBanner : CustomItem("haste_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.YELLOW_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Haste Banner")
                .color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Haste", Activation.RIGHT_CLICK),
                ItemLore.description("Grant Haste to nearby players for 30s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("Forged for those who carve through stone and time."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.YELLOW_WOOL, Material.YELLOW_WOOL, Material.YELLOW_WOOL,
            Material.YELLOW_WOOL, Material.GOLDEN_PICKAXE, Material.YELLOW_WOOL,
            null, Material.STICK, null
        ))
    }

    fun isHasteBanner(item: ItemStack?): Boolean {
        return item?.type == Material.YELLOW_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}