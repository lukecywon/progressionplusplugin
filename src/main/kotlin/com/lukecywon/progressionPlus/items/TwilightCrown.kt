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

object TwilightCrown : CustomItem("twilight_crown", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_HELMET)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(Component.text("👑 Twilight Crown").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))

        meta.lore(listOf(
            Component.text("§7The crown once worn by a forgotten monarch."),
            Component.text("§8Its power resonates with ancient relics..."),
            Component.text("§8Whispers stir when paired with the §dOld King's Blade§8."),
            Component.text("§7Those who listen closely may command what lies beyond.")
        ))

        meta.addAttributeModifier(
            Attribute.ARMOR,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "armor"),
                0.0, // purely cosmetic, no protection
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.setCustomModelData(9050)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isTwilightCrown(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_HELMET) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}

