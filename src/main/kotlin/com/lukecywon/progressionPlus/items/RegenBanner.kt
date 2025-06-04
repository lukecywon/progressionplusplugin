package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object RegenBanner : CustomItem("regen_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.PINK_BANNER)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Regen Banner")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(listOf(
            Component.text("Right click to grant Regeneration").color(NamedTextColor.GRAY),
            Component.text("to nearby players for 30s").color(NamedTextColor.GRAY)
        ))

        meta.setCustomModelData(3003)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isRegenBanner(item: ItemStack?): Boolean {
        return item?.type == Material.PINK_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}