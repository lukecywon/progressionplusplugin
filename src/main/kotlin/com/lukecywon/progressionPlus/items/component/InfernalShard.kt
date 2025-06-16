package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object InfernalShard : CustomItem("infernal_shard", Rarity.COMPONENT) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Infernal Shard")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("A searing shard imbued with nether heat."),
                ItemLore.separator(),
                ItemLore.lore("It radiates a hunger for ignition.")
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<Material?> {
        return listOf(
            Material.BLAZE_ROD, Material.WITHER_ROSE, Material.BLAZE_ROD,
            Material.NETHER_WART, Material.NETHERITE_SCRAP, Material.NETHER_WART,
            Material.BLAZE_ROD, Material.WITHER_ROSE, Material.BLAZE_ROD
        )
    }
}
