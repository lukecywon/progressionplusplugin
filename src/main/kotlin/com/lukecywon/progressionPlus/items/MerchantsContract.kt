package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object MerchantsContract : CustomItem("merchants_contract", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Merchant's Contract")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Trade with Players", Activation.RIGHT_CLICK),
                ItemLore.description("Open a menu to request trades with others online."),
                ItemLore.separator(),
                ItemLore.lore("Signed in ink, bound by trust.")
            )
        )
        meta.setCustomModelData(9052)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isMerchantsContract(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.WRITTEN_BOOK) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
