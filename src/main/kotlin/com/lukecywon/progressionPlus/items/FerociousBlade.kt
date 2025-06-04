package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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
        meta.displayName(
            Component.text("Ferocious Blade")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Relentless Blow", Activation.HIT),
                ItemLore.description("50% chance to strike a second time on hit"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("Strikes twice when fate allows, always hungry for more."),
                ItemLore.rarity(getRarity())
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