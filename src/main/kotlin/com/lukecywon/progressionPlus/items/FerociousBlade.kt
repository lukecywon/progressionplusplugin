package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object FerociousBlade : CustomItem("ferocious_blade", Rarity.EPIC) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.STONE_SWORD)
        val meta = item.itemMeta ?: return item

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        // Set display name and lore
        meta.displayName(Component.text("⚔ Ferocious Blade").color(Rarity.EPIC.color))

        meta.lore(
            listOf(
                Component.text("A blade that thirsts for blood...").color(NamedTextColor.DARK_RED),
                Component.text("50% chance to strike twice!").color(NamedTextColor.GOLD)
            )
        )

        meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE)
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED)
        meta.addAttributeModifier(
            Attribute.ATTACK_DAMAGE,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "damage"),
                5.0,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )
        meta.addAttributeModifier(
            Attribute.ATTACK_SPEED,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "damage"),
                -2.0,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )

        item.itemMeta = meta

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        return applyMeta(item)
    }
}