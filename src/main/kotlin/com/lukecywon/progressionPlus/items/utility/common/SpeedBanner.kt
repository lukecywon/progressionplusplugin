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

object SpeedBanner : CustomItem("speed_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.LIGHT_BLUE_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Speed Banner")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Speed", Activation.RIGHT_CLICK),
                ItemLore.description("Grant Speed to nearby players for 30s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("For those who chase victory with every step."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "speed_banner")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIGHT_BLUE_WOOL,
            Material.LIGHT_BLUE_WOOL, Material.SUGAR, Material.LIGHT_BLUE_WOOL,
            null, Material.STICK, null
        ))
    }
}
