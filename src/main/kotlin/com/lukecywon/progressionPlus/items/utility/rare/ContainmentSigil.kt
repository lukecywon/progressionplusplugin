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
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ContainmentSigil : CustomItem("containment_sigil", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Containment Sigil")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Mark of Binding", Activation.HIT),
                ItemLore.description("Strike a mob to mark it for containment."),
                ItemLore.description("Killing the marked mob drops its spawn egg."),
                ItemLore.description("Does not work on players or bosses."),
                ItemLore.separator(),
                ItemLore.lore("Marks the vessel for transfer.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "containment_sigil")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }
}
