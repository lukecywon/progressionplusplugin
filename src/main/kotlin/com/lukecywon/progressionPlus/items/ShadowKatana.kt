package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import com.lukecywon.progressionPlus.mechanics.ItemLore
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

object ShadowKatana : CustomItem("shadow_katana", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Shadow Katana")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Shadow Dash", Activation.RIGHT_CLICK),
                ItemLore.description("Dash forward and slash through enemies"),
                ItemLore.description("Leaves a slash trail for 5s"),
                ItemLore.cooldown(15),
                ItemLore.separator(),
                ItemLore.lore("Slashes faster than the eye can see."),
            )
        )

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "attack_damage"),
            7.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.setCustomModelData(9057)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isShadowKatana(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.NETHERITE_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
