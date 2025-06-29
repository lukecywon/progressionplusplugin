package com.lukecywon.progressionPlus.items.utility.uncommon

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object MaxHeartFruit : CustomItem("max_heart_fruit", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_APPLE)
        val meta = item.itemMeta

        meta.displayName(Component.text("Max Heart Fruit").color(NamedTextColor.RED))
        meta.lore(
            listOf(
                ItemLore.abilityuse("Heart Bloom", Activation.CONSUME),
                ItemLore.description("Permanently increases max health by 1 heart"),
                ItemLore.description("Cannot be used above 10 hearts"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("The fruit of vitality, blooming only for those at their weakest."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.VINE, null, Material.VINE,
            Material.VINE, Material.GOLDEN_APPLE, Material.VINE,
            null, Material.EMERALD, null
        ))
    }
}