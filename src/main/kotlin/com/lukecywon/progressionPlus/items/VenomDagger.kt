package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object VenomDagger : CustomItem("venom_dagger", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.WOODEN_SWORD)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Venom Dagger")
                .color(NamedTextColor.DARK_GREEN)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Venom Strike", Activation.HIT),
                ItemLore.description("Inflicts Poison and Weakness on hit"),
                ItemLore.cooldown(7),
                ItemLore.separator(),
                ItemLore.lore("Drenched in a vile toxin…")
            )
        )

        meta.setCustomModelData(9028)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isVenomDagger(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.WOODEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
