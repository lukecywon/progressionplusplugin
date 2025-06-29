package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey

object SunscorchedEmber : CustomItem("sunscorched_ember", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLAZE_POWDER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Sunscorched Ember")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("Burned into being by ancient sunfire."),
                ItemLore.separator(),
                ItemLore.lore("Once buried beneath golden sands."),
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "sunscorched_ember")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Found in Desert Temple chests.")
    }
}
