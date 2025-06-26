package com.lukecywon.progressionPlus.items.armor.uncommon

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.RecipeChoice

object NocturnHood : CustomItem("nocturn_hood", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_HELMET)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Nocturn Hood")
                .color(NamedTextColor.DARK_AQUA)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Night Vision", Activation.PASSIVE),
                ItemLore.description("Grants Night Vision while worn"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("Light pierces even the deepest dark."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "nocturn_hood")

        // Remove armor protection
        val noArmor = AttributeModifier(
            NamespacedKey.minecraft("no_armor"),
            -2.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HEAD
        )
        meta.addAttributeModifier(Attribute.ARMOR, noArmor)

        meta.persistentDataContainer.set(
            NamespacedKey(ProgressionPlus.getPlugin(), "id"),
            PersistentDataType.STRING,
            "nocturn_hood"
        )
        item.itemMeta = meta
        return applyMeta(item)
    }
    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.TINTED_GLASS, Material.IRON_INGOT, Material.TINTED_GLASS,
            Material.IRON_INGOT, null, Material.IRON_INGOT,
            null,null,null
        ))
    }
}
