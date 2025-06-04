package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ResonantBlade : CustomItem("resonant_blade", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SWORD)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Resonant Blade")
                .color(NamedTextColor.BLUE)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(listOf(
            Component.text("“Strike true, strike fast…”")
                .color(NamedTextColor.GRAY)
                .decoration(TextDecoration.ITALIC, false),
            Component.text("After 5 hits in 6s,")
                .color(NamedTextColor.BLUE)
                .decoration(TextDecoration.ITALIC, false),
            Component.text("the 5th hit deals +5 bonus damage.")
                .color(NamedTextColor.BLUE)
                .decoration(TextDecoration.ITALIC, false)
        ))

        meta.setCustomModelData(9030)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isResonantBlade(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
