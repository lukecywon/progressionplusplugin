package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object AshenWarhammer : CustomItem("ashen_warhammer", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_AXE)
        val meta = item.itemMeta

        meta.displayName(Component.text("Ashen Warhammer", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
        meta.lore(listOf(
            Component.text("On kill, leaves a lingering cloud", NamedTextColor.GRAY),
            Component.text("that weakens foes nearby.", NamedTextColor.GRAY),
            Component.text("\"Ashes to ashes, strength to dust.\"", NamedTextColor.DARK_RED, TextDecoration.ITALIC)
        ))

        meta.setCustomModelData(9031)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isAshenWarhammer(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_AXE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
