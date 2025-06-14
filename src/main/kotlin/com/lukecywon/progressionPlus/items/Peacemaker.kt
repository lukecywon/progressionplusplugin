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
import org.bukkit.persistence.PersistentDataType
import java.util.*

object Peacemaker : CustomItem("peacemaker", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_HOE) // You can change the model if needed
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Peacemaker")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Bullet Load", Activation.SHIFT_RIGHT_CLICK),
                ItemLore.description("Hold Shift + Right Click to load 1 bullet (2s)"),
                ItemLore.abilityuse("Shoot", Activation.RIGHT_CLICK),
                ItemLore.description("Fire a bullet with slight spread"),
                ItemLore.abilityuse("Fan the Hammer", Activation.LEFT_CLICK),
                ItemLore.description("Unload all bullets rapidly in a wide arc"),
                ItemLore.cooldown(1), // 1 second cooldown
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("It's high noon."),
            )
        )

        // Optional: reduce attack speed so it's not used as a melee weapon
        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            -2.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        val speedModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_speed"),
            -3.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, speedModifier)

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "peacemaker")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }
}
