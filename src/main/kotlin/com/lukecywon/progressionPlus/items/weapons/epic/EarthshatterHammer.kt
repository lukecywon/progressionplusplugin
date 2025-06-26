package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

object EarthshatterHammer : CustomItem("earthshatter_hammer", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_AXE)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Earthshatter Hammer")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Earthshatter", Activation.RIGHT_CLICK),
                ItemLore.description("Launch blocks upward in a shockwave"),
                ItemLore.cooldown(30),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A mighty blow that shakes the land.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "earthshatter_hammer")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isEarthshatterHammer(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.NETHERITE_AXE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
