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

object PhantomCharm : CustomItem("phantom_charm", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.AMETHYST_SHARD)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Phantom Charm")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Phantom Repellent", Activation.OFFHAND),
                ItemLore.description("Prevents phantoms from targeting you."),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("It hums softly in the night air.")
            )
        )

        meta.setCustomModelData(9032)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isPhantomCharm(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.AMETHYST_SHARD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
