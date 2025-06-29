package com.lukecywon.progressionPlus.items.weapons.rare

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object OldKingsBlade : CustomItem("old_kings_blade", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.GOLDEN_SWORD)
        item = applyBaseDamage(item, 9.0)
        item = applyBaseAttackSpeed(item, 1.4)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Old King's Blade")
                .color(Rarity.EPIC.color)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Raise Undead", Activation.RIGHT_CLICK),
                ItemLore.description("Summon undead followers to fight for you"),
                ItemLore.abilityuse("Raise Undead", Activation.SHIFT_RIGHT_CLICK),
                ItemLore.description("Dismiss your army"),
                ItemLore.cooldown(180),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Once wielded by a long-dead monarch."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "old_kings_blade")
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf("§7Looted from Desert Temples.")
    }
}
