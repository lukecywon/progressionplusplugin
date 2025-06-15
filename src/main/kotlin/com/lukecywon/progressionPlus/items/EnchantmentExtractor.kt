package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object EnchantmentExtractor : CustomItem("enchantment_extractor", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.QUARTZ)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Enchantment Extractor")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Extract Enchantments", Activation.OFFHAND),
                ItemLore.description("Destroys the item in your main hand and converts its enchantments into books."),
                ItemLore.separator(),
                ItemLore.lore("Preserve knowledge at the cost of material.")
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }
}
