package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ExecutionerSword : CustomItem("executioner_sword", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SWORD)
        val meta = item.itemMeta!!

        meta.displayName(Component.text("☠ Executioner Sword").color(Rarity.RARE.color))
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        meta.lore(listOf(
            Component.text("The final sight of many men.", NamedTextColor.GRAY),
            Component.text("Right-click to cleave in front of yourself!", NamedTextColor.YELLOW),
        ))

        item.itemMeta = meta

        return item
    }
}