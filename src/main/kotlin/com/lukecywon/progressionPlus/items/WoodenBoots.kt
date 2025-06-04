package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack

object WoodenBoots : CustomItem("wooden_boots", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.LEATHER_BOOTS)
        val meta = item.itemMeta

        meta.displayName(Component.text("Wooden Boots", Rarity.COMMON.color))

        meta.removeAttributeModifier(Attribute.ARMOR)
        meta.addAttributeModifier(
            Attribute.ARMOR,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "boots_armor"),
                1.5,
                AttributeModifier.Operation.ADD_NUMBER,
            )
        )
        item.itemMeta = meta
        return applyMeta(item)
    }
}