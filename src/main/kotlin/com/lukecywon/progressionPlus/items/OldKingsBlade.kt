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

object OldKingsBlade : CustomItem("old_kings_blade", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_SWORD)
        val meta = item.itemMeta ?: return item

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("☠ Old King's Blade").color(Rarity.EPIC.color))

        meta.displayName(
            Component.text("Old King's Blade")
                .color(NamedTextColor.DARK_BLUE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Raise Undead", Activation.RIGHT_CLICK),
                ItemLore.description("Summon undead followers to fight for you"),
                ItemLore.abilityuse("Raise Undead", Activation.SHIFT_RIGHT_CLICK),
                ItemLore.description("Dismiss your army"),
                ItemLore.cooldown(180),
                ItemLore.separator(),
                ItemLore.lore("Once wielded by a long-dead monarch."),
            )
        )

        meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE)
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED)

        meta.addAttributeModifier(
            Attribute.ATTACK_DAMAGE,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
                6.0,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HAND
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isOldKingsBlade(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, org.bukkit.persistence.PersistentDataType.BYTE)
    }
}
