package com.lukecywon.progressionPlus.items


import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

object BerserkerSword : CustomItem("berserker_sword", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.STONE_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text()
                .append(Component.text("[").decorate(TextDecoration.OBFUSCATED).color(NamedTextColor.DARK_RED))
                .append(Component.text("Berserker Sword").color(NamedTextColor.DARK_RED))
                .append(Component.text("]").decorate(TextDecoration.OBFUSCATED).color(NamedTextColor.DARK_RED))
                .build()
        )

        meta.lore(listOf(
            Component.text("Unleash your rage at a deadly cost.").color(NamedTextColor.RED),
            Component.text("Halves your hearts while held.").color(NamedTextColor.DARK_RED),
            Component.text("Right-click after 10s to gain Strength I").color(NamedTextColor.GOLD),
            Component.text("But suffer Hunger III").color(NamedTextColor.GRAY)
        ))

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