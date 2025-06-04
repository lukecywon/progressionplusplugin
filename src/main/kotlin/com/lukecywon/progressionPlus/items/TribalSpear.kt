package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object TribalSpear : CustomItem("tribal_spear", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.TRIDENT)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("⚔ Tribal Spear", Rarity.RARE.color))

        meta.lore(listOf(
            Component.text("A powerful ancient weapon.", NamedTextColor.GRAY),
            Component.text("Right-click to throw it!", NamedTextColor.YELLOW),
        ))

        meta.addEnchant(Enchantment.LOYALTY, 1, false)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

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
                -3.0,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )





        item.itemMeta = meta
        return applyMeta(item)
    }
}