package com.lukecywon.progressionPlus.items.weapons.rare

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object OldKingsBlade : CustomItem("old_kings_blade", Rarity.RARE) {

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.GOLDEN_SWORD)
        item = applyBaseDamage(item, 9.0)
        item = applyBaseAttackSpeed(item)
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

        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isOldKingsBlade(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, org.bukkit.persistence.PersistentDataType.BYTE)
    }
}
