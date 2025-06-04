package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object VenomDagger : CustomItem("venom_dagger", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WOODEN_SWORD)
        val meta = item.itemMeta

        meta.displayName(Component.text("Venom Dagger").color(NamedTextColor.DARK_GREEN))
        meta.lore(listOf(
            Component.text("Coated in something unpleasant…").color(NamedTextColor.GREEN),
            Component.text("Applies Poison and Weakness on hit.").color(NamedTextColor.GRAY),
            Component.text("Cooldown: 7 seconds").color(NamedTextColor.DARK_GRAY)
        ))

        meta.setCustomModelData(9028)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isVenomDagger(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.WOODEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
