package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.persistence.PersistentDataType

object SoulrendScythe : CustomItem("soulrend_scythe", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.IRON_SWORD)
        item = applyBaseDamage(item, 9.0)
        item = applyBaseAttackSpeed(item, -0.8)
        val meta = item.itemMeta!!

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Soulrend Scythe")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Lifesteal", Activation.PASSIVE),
                ItemLore.description("Deals +1 damage per debuff on you"),
                ItemLore.description("Reduced boost from bad omen"),
                ItemLore.description("Restores 25% of damage dealt as health"),
                ItemLore.cooldown(0),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("The more you suffer, the deeper it carves."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "soulrend_scythe")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isSoulrendScythe(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
