package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object HealthCrystal : CustomItem("health_crystal", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENCHANTED_GOLDEN_APPLE)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("Health Crystal").color(NamedTextColor.LIGHT_PURPLE))
        meta.lore(listOf(Component.text("Eat to gain +1 max heart (up to 20)!").color(NamedTextColor.AQUA)))
        meta.setCustomModelData(9021)
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
