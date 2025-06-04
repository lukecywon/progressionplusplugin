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
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object BerserkerSword : CustomItem("berserker_sword", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.STONE_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Berserker Sword")
                .color(NamedTextColor.DARK_RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Adrenaline rush", Activation.RIGHT_CLICK),
                ItemLore.description("Grants the user strength and hunger for 15s"),
                ItemLore.description("Halves the max health of the wielder"),
                ItemLore.cooldown(30),
                ItemLore.separator(),
                ItemLore.lore("A reckless edge, fueled by fury and blood."),
            )
        )

        // Optional: Add diamond-equivalent damage (stone base 5 + 2 = 7)
        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "damage"),
            7.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.setCustomModelData(9025)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isBerserkerSword(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.STONE_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }


}