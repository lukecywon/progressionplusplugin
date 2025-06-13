package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object HeadsmansEdge : CustomItem("headsmans_edge", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_AXE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Headsman's Edge")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("20% chance to behead mobs with skulls", Activation.KILL),
                ItemLore.description("Works on skeletons, zombies, creepers, piglins and players."),
                ItemLore.separator(),
                ItemLore.lore("The axe of judgment, used by executioners of old.")
            )
        )

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "headsman_damage"),
            7.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "headsmans_edge")
        item.itemMeta = meta

        return applyMeta(item)
    }
}
