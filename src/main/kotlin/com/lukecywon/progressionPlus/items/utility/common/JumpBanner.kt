package com.lukecywon.progressionPlus.items.utility.common

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
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

object JumpBanner : CustomItem("jump_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.LIME_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Jump Banner")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Jump Boost", Activation.RIGHT_CLICK),
                ItemLore.description("Grant Jump Boost to nearby players for 30s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("For those who rise above, when the ground won’t carry them far enough."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.LIME_WOOL, Material.LIME_WOOL, Material.LIME_WOOL,
            Material.LIME_WOOL, Material.RABBIT_HIDE, Material.LIME_WOOL,
            null, Material.STICK, null
        ))
    }
}
