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
        meta.lore(listOf(
            Component.text("“Strike where it hurts most.”").color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false),
            Component.text("Every 4th hit ignores armor.")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.ITALIC, false)
        ))

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
