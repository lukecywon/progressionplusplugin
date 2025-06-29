package com.lukecywon.progressionPlus.items.utility.uncommon

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

object VerdantCleaver : CustomItem("verdant_cleaver", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_AXE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Verdant Cleaver")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Timber!", Activation.BLOCK_BROKEN),
                ItemLore.description("Chops down entire trees when breaking the base log."),
                ItemLore.abilityuse("Change vein size", Activation.RIGHT_CLICK),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("The lumberjack’s best friend.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "verdant_cleaver")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    val sizeKey = NamespacedKey(plugin, "verdant_cleaver_size")

    fun getVeinSize(item: ItemStack?): Int {
        val meta = item?.itemMeta ?: return 64
        return meta.persistentDataContainer.get(sizeKey, PersistentDataType.INTEGER) ?: 64
    }

    fun cycleVeinSize(item: ItemStack): Int {
        val meta = item.itemMeta ?: return 64
        val current = getVeinSize(item)
        val next = when (current) {
            32 -> 64
            64 -> 128
            else -> 32
        }
        meta.persistentDataContainer.set(sizeKey, PersistentDataType.INTEGER, next)
        item.itemMeta = meta
        return next
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.MOSSY_COBBLESTONE, Material.VINE,
            null, Material.IRON_AXE, Material.VINE,
            null, Material.STICK, null
        ))
    }
}