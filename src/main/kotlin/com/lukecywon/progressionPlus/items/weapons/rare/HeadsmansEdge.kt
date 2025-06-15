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
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object HeadsmansEdge : CustomItem("headsmans_edge", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.STONE_SWORD)
        item = applyBaseDamage(item, 17.0)
        item = applyBaseAttackSpeed(item)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Headsman's Edge")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("20% chance to behead mobs with skulls", Activation.KILL),
                ItemLore.description("Works on skeletons, zombies, creepers, piglins and players."),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("The axe of judgment, used by executioners of old.")
            )
        )

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "headsmans_edge")
        item.itemMeta = meta

        return applyMeta(item)
    }
}
