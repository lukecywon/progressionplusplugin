package com.lukecywon.progressionPlus.items.component

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object AetherCore : CustomItem("aether_core", Rarity.COMPONENT) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.HEART_OF_THE_SEA)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Aether Core")
                .color(NamedTextColor.AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.description("An ancient core pulsating with skybound energy."),
                ItemLore.separator(),
                ItemLore.lore("Its presence lifts the world ever so slightly.")
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<Material?> {
        return listOf(
            Material.ELYTRA, Material.ELYTRA, Material.ELYTRA,
            Material.ELYTRA, Material.NETHER_STAR, Material.ELYTRA,
            Material.ELYTRA, Material.ELYTRA, Material.ELYTRA
        )
    }
}
