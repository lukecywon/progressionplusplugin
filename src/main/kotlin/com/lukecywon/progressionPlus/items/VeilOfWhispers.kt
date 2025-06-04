package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object VeilOfWhispers : CustomItem("veil_of_whispers", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ENDER_EYE)
        val meta = item.itemMeta

        meta.displayName(Component.text("Veil of Whispers").color(NamedTextColor.LIGHT_PURPLE))
        meta.lore(listOf(
            Component.text("Hold to reveal entities through walls."),
            Component.text("Right-click to phase through blocks for 10s."),
            Component.text("Cooldown: 15s")
        ))
        meta.setCustomModelData(9051)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isVeilOfWhispers(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.ENDER_EYE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}