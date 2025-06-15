package com.lukecywon.progressionPlus.items.utility.rare

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object EarthSplitter : CustomItem("earth_splitter", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SHOVEL)
        val meta = item.itemMeta ?: return item

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Earth Splitter")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Excavator Mode", Activation.BLOCK_BROKEN),
                ItemLore.description("Tears through patches of soft earth in one scoop."),
                ItemLore.abilityuse("Change vein size", Activation.RIGHT_CLICK),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("Forged to reshape the land itself.")
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isEarthSplitter(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_SHOVEL) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    val sizeKey = NamespacedKey(plugin, "earth_splitter_size")

    fun getVeinSize(item: ItemStack?): Int {
        val meta = item?.itemMeta ?: return 64
        return meta.persistentDataContainer.get(sizeKey, PersistentDataType.INTEGER) ?: 64
    }

    fun cycleVeinSize(item: ItemStack): Int {
        val meta = item.itemMeta ?: return 64
        val current = getVeinSize(item)
        val next = when (current) {
            32 -> 64
            64 -> 128
            else -> 32
        }
        meta.persistentDataContainer.set(sizeKey, PersistentDataType.INTEGER, next)
        item.itemMeta = meta
        return next
    }
}
