package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType

object SoulrendScythe : CustomItem("soulrend_scythe", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_HOE)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("Soulrend Scythe").color(NamedTextColor.DARK_RED).decorate(TextDecoration.BOLD))
        meta.lore(listOf(
            Component.text("§7Deals §c+1§7 damage per debuff on you."),
            Component.text("§7Restores §c25%§7 of damage as health."),
        ))

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            5.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.setCustomModelData(9045)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isSoulrendScythe(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_HOE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
