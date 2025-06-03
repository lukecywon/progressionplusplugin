package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object Snowglobe : CustomItem("snowglobe", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.SNOWBALL)
        val meta = item.itemMeta

        meta.displayName(Component.text("Snowglobe").color(NamedTextColor.AQUA))
        meta.lore(listOf(
            Component.text("Right click to create a slowing snow globe!").color(NamedTextColor.GRAY),
            Component.text("All entities except you move 50% slower for 10s").color(NamedTextColor.DARK_AQUA)
        ))
        meta.setCustomModelData(9027) // Pick any custom model data you want
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isSnowglobe(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.SNOWBALL) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}