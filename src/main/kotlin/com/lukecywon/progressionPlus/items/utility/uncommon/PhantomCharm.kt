package com.lukecywon.progressionPlus.items.utility.uncommon

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

object PhantomCharm : CustomItem("phantom_charm", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.AMETHYST_SHARD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Phantom Charm")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Phantom Repellent", Activation.OFFHAND),
                ItemLore.description("Prevents phantoms from targeting you."),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("It hums softly in the night air.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "phantom_charm")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.PHANTOM_MEMBRANE, Material.STRING, Material.PHANTOM_MEMBRANE,
            Material.GLOW_INK_SAC, Material.PHANTOM_MEMBRANE, Material.GLOW_INK_SAC,
            null, Material.GLOW_BERRIES, null
        ))
    }
}
