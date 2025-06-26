package com.lukecywon.progressionPlus.items.weapons.rare

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

object ExecutionerSword : CustomItem("executioner_sword", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SWORD)
        val meta = item.itemMeta!!

        meta.displayName(Component.text("Executioner Sword").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD))
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        meta.lore(listOf(
            ItemLore.abilityuse("Final Verdict", Activation.RIGHT_CLICK),
            ItemLore.description("Cleaves all enemies in front of you"),
            ItemLore.cooldown(0),
            ItemLore.stats(item),
            ItemLore.separator(),
            ItemLore.lore("The final sight of many men."),
        ))

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "executioner_sword")

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.IRON_BLOCK, null,
            null, Material.IRON_BLOCK, null,
            null, Material.STICK, null
        ))
    }
}