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

object RogueSword : CustomItem("rogue_sword", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.GOLDEN_SWORD)
        item = applyBaseDamage(item, 4.0)
        item = applyBaseAttackSpeed(item, 2.2)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Rogue Sword")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Swift Dash", Activation.RIGHT_CLICK),
                ItemLore.description("Gain Speed for 10 seconds"),
                ItemLore.cooldown(20),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A thief’s resolve, a killer’s edge."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "rogue_sword")

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.DIAMOND, null,
            null, Material.GOLDEN_SWORD, null,
            null, Material.STICK, null
        ))
    }
}