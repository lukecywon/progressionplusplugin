package com.lukecywon.progressionPlus.items.weapons.rare

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object FerociousBlade : CustomItem("ferocious_blade", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.STONE_SWORD)
        item = applyBaseDamage(item, 10.0)
        item = applyBaseAttackSpeed(item, -0.4)
        val meta = item.itemMeta!!

        // Set display name and lore
        meta.displayName(
            Component.text("Ferocious Blade")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Relentless Blow", Activation.HIT),
                ItemLore.description("50% chance to strike a second time on hit"),
                ItemLore.cooldown(0),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Strikes twice when fate allows, always hungry for more."),
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }
}