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

object EchoCore : CustomItem("echo_core", Rarity.COMPONENT) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ECHO_SHARD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Echo Core")
                .color(NamedTextColor.DARK_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("Resonates with silence and dread."),
                ItemLore.separator(),
                ItemLore.lore("Whispers echo from within."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "echo_core")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Found in Ancient City chests.")
    }
}
