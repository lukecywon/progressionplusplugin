package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object AbsorptionBanner : CustomItem("absorption_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ORANGE_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Absorption Banner")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(listOf(
            Component.text("Right click to grant Absorption").color(NamedTextColor.GRAY),
            Component.text("to nearby players for 30s").color(NamedTextColor.GRAY)
        ))

        meta.setCustomModelData(3005)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isAbsorptionBanner(item: ItemStack?): Boolean {
        return item?.type == Material.ORANGE_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}