package com.lukecywon.progressionPlus.items.utility.legendary

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object SnowGlobe : CustomItem("snowglobe", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.SNOWBALL)
        val meta = item.itemMeta!!

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Snow Globe")
                .color(NamedTextColor.AQUA)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Frost Zone", Activation.RIGHT_CLICK),
                ItemLore.description("Creates a slowing snow globe"),
                ItemLore.description("All entities except you move 50% slower for 10s"),
                ItemLore.cooldown(15),
                ItemLore.separator(),
                ItemLore.lore("A swirling prison of frost, where time itself staggers."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }
}