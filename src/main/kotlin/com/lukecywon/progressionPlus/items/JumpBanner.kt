package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object JumpBanner : CustomItem("jump_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.LIME_BANNER)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Jump Banner")
                .color(NamedTextColor.GREEN)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(listOf(
            Component.text("Right click to grant Jump Boost").color(NamedTextColor.GRAY),
            Component.text("to nearby players for 30s").color(NamedTextColor.GRAY)
        ))

        meta.setCustomModelData(3004)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isJumpBanner(item: ItemStack?): Boolean {
        return item?.type == Material.LIME_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}
