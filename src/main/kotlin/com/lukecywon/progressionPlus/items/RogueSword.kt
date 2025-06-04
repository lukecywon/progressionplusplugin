package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object RogueSword : CustomItem("rogue_sword", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Rogue Sword")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(listOf(
            Component.text("Swift and deadly.").color(NamedTextColor.GRAY),
            Component.text("Right-click to gain Speed I for 10s.").color(NamedTextColor.GREEN),
            Component.text("Cooldown: 20 seconds").color(NamedTextColor.DARK_GRAY)
        ))

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            5.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        val attackspeedmodifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_speed"),
            0.8,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, attackspeedmodifier)

        meta.setCustomModelData(9026)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isRogueSword(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}