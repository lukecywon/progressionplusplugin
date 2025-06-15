package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object AshenWarhammer : CustomItem("ashen_warhammer", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.GOLDEN_AXE)
        item = applyBaseDamage(item)
        item = applyBaseAttackSpeed(item)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Ashen Warhammer")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Wither", Activation.KILL),
                ItemLore.description("Leaves a lingering smog that withers away enemies for 5s"),
                ItemLore.cooldown(10),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A burning curse unleashed on those who fall."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isAshenWarhammer(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_AXE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
