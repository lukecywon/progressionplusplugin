package com.lukecywon.progressionPlus.items.armor.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack

object RocketHarness : CustomItem("rocket_harness", Rarity.EPIC, false) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.RED_HARNESS)
        val meta = item.itemMeta

        meta.displayName(Component.text("Rocket Harness").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))

        meta.lore(listOf(
            ItemLore.lore("§7The harness once worn by a weeping warden of the skies.")
        ))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Sorrowful March", Activation.PASSIVE),
                ItemLore.description("Doubles the fly speed of a Happy Ghast"),
                ItemLore.separator(),
                ItemLore.lore("A remnant of ancient wrath, still echoing with vengeance."),
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }
}