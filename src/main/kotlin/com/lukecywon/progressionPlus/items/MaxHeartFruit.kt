package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object MaxHeartFruit : CustomItem("max_heart_fruit", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_APPLE)
        val meta = item.itemMeta
        meta.displayName(Component.text("Max Heart Fruit").color(NamedTextColor.RED))
        meta.lore(listOf(Component.text("Eat to gain +1 max heart (up to 10)!").color(NamedTextColor.GOLD)))
        meta.setCustomModelData(9020)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isHeartFruit(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_APPLE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}