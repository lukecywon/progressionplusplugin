package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object ShadowKatana : CustomItem("shadow_katana", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Shadow Katana")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(listOf(
            Component.text("Slashes faster than the eye can see.").color(NamedTextColor.GRAY),
            Component.text("Leaves a lingering slash trail for 5 seconds.").color(NamedTextColor.GRAY),
            Component.text("Right-click to dash.").color(NamedTextColor.LIGHT_PURPLE),
            Component.text("Cooldown: 15 seconds").color(NamedTextColor.DARK_GRAY)
        ))

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            7.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.setCustomModelData(9057)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isShadowKatana(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.NETHERITE_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
