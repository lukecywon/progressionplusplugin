package com.lukecywon.progressionPlus.items.utility.rare

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

object TectonicFang : CustomItem("tectonic_fang", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_PICKAXE)
        val meta = item.itemMeta ?: return item

        meta.displayName(
            Component.text("Tectonic Fang")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Tectonic Shatter", Activation.BLOCK_BROKEN),
                ItemLore.description("Shatters veins of the same block type in one strike."),
                ItemLore.abilityuse("Change vein size", Activation.RIGHT_CLICK),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A primal fang from the heart of the world.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "tectonic_fang")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    val sizeKey = NamespacedKey(plugin, "tectonic_fang_size")

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
            Material.SKELETON_SKULL, Material.NETHERITE_INGOT, Material.SKELETON_SKULL,
            Material.RAW_COPPER_BLOCK, Material.DIAMOND_PICKAXE, Material.RAW_GOLD_BLOCK,
            Material.ECHO_SHARD, Material.RAW_IRON_BLOCK, Material.ECHO_SHARD
        ))
    }
}
