package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object LuckTalisman : CustomItem("luck_talisman", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.EMERALD)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Luck Talisman")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Fortune's Blessing", Activation.RIGHT_CLICK),
                ItemLore.description("Grants Luck IV for 15 seconds"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("A charm whispered with fate, tilting odds in your favor."),
            )
        )

        meta.setCustomModelData(9035)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isLuckTalisman(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.EMERALD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
