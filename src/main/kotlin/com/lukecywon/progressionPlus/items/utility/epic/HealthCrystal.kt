package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object HealthCrystal : CustomItem("health_crystal", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Health Crystal")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Soul Fusion", Activation.CONSUME),
                ItemLore.description("Permanently increases max health by 1 heart"),
                ItemLore.description("Cannot be used when less than 10 hearts"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A shard of life itself, yearning to become one with its bearer."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isHealthCrystal(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.ENCHANTED_GOLDEN_APPLE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
