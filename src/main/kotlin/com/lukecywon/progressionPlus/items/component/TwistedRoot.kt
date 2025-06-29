package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object TwistedRoot : CustomItem("twisted_root", Rarity.COMPONENT, true) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.STICK)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Twisted Root")
                .color(NamedTextColor.DARK_GREEN)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("Saturated with ancient woodland magic."),
                ItemLore.separator(),
                ItemLore.lore("It still writhes when held too long."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "twisted_root")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Looted from Woodland Mansions.")
    }
}
