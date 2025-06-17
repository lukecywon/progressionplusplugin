package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object WardensHeart : CustomItem("wardens_heart", Rarity.COMPONENT) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHER_STAR)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Warden's Heart")
                .color(NamedTextColor.DARK_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("A pulsating remnant torn from the Warden’s chest."),
                ItemLore.separator(),
                ItemLore.lore("It beats with the echoes of the Deep Dark.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "wardens_heart")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Dropped by the Warden.")
    }
}
