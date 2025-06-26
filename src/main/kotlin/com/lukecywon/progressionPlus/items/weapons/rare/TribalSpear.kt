package com.lukecywon.progressionPlus.items.weapons.rare

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
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

object TribalSpear : CustomItem("tribal_spear", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.TRIDENT)
        item = applyBaseDamage(item, 5.0)
        item = applyBaseAttackSpeed(item)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Tribal Spear")
                .color(NamedTextColor.DARK_GREEN)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Spear Throw", Activation.RIGHT_CLICK),
                ItemLore.description("Toss a sharp stick forward"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A weapon passed down through forgotten bloodlines."),
            )
        )

        meta.addEnchant(Enchantment.LOYALTY, 1, false)

        item.itemMeta = meta
        return applyMeta(item)
    }
}