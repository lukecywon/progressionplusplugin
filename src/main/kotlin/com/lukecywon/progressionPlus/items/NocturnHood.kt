package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlotGroup

object NocturnHood : CustomItem("nocturn_hood", Rarity.RARE) {
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
                Component.text("§7Grants §9Night Vision §7while worn."),
                Component.text("§8Light pierces even the deepest dark.")
            )
        )

        // Remove armor protection
        val noArmor = AttributeModifier(
            NamespacedKey.minecraft("no_armor"),
            -2.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HEAD
        )
        meta.addAttributeModifier(Attribute.ARMOR, noArmor)

        meta.setCustomModelData(9055) // Your model data ID
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isNocturnHood(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_HELMET) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
