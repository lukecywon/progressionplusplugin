package com.lukecywon.progressionPlus.items.weapons.uncommon

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

object VenomDagger : CustomItem("venom_dagger", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WOODEN_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Venom Dagger")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Venom Strike", Activation.HIT),
                ItemLore.description("Inflicts Poison and Weakness on hit"),
                ItemLore.cooldown(7),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Drenched in a vile toxin…")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "venom_dagger")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isVenomDagger(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.WOODEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.POISONOUS_POTATO, null,
            null, Material.PALE_OAK_LOG, null,
            null, Material.STICK, null
        ))
    }
}
