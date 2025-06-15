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
        var item = ItemStack(Material.DIAMOND_SWORD)
        item = applyBaseDamage(item, 15.0)
        item = applyBaseAttackSpeed(item, -1.0)
        val meta = item.itemMeta!!

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
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Strike where it hurts most."),
            )
        )

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
