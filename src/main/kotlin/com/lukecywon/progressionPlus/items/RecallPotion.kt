package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object RecallPotion : CustomItem("recall_potion", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.POTION)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("Recall Potion").color(NamedTextColor.AQUA))
        meta.lore(listOf(Component.text("Drink to teleport to your spawn after 5s").color(NamedTextColor.GRAY)))
        meta.setCustomModelData(9022)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isRecallPotion(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.POTION) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
