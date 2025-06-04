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
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object SoulPiercer : CustomItem("soul_piercer", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.DIAMOND_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Soul Piercer")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Armor Pierce", Activation.PASSIVE),
                ItemLore.description("Every 5th hit ignores 80% of enemy armor"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("Strike where it hurts most."),
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.isUnbreakable = true
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            8.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)
        val attackspeedmodifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_speed"),
            -2.6,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, attackspeedmodifier)

        meta.setCustomModelData(9032)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isSoulPiercer(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.DIAMOND_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
