package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object HasteBanner : CustomItem("haste_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.YELLOW_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Haste Banner")
                .color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(listOf(
            Component.text("Right click to grant Haste I").color(NamedTextColor.GRAY),
            Component.text("to nearby players for 30s").color(NamedTextColor.GRAY)
        ))

        meta.setCustomModelData(3002)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isHasteBanner(item: ItemStack?): Boolean {
        return item?.type == Material.YELLOW_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}