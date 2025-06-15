package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
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

object PhoenixTotem : CustomItem("phoenix_totem", Rarity.EPIC) {
    val phoenixKey = NamespacedKey(ProgressionPlus.instance, "phoenix_totem")

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.TOTEM_OF_UNDYING)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Phoenix Totem")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Rebirth", Activation.DEATH),
                ItemLore.description("Prevents death and brings you to your spawn"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("When all seems lost, it saves you in a blaze of fire."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "phoenix_totem")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isPhoenixTotem(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.TOTEM_OF_UNDYING) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(phoenixKey, PersistentDataType.BYTE)
    }
}

