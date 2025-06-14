package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ResonantBlade : CustomItem("resonant_blade", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SWORD)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Resonant Blade")
                .color(NamedTextColor.BLUE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Resonance", Activation.HIT),
                ItemLore.description("Land 5 consecutive hits,"),
                ItemLore.description("each within 3s of the last,"),
                ItemLore.description("to deal +5 bonus damage on the final strike."),
                ItemLore.cooldown(0),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Strike in rhythm, and it answers with ruin."),
            )
        )

        meta.setCustomModelData(9030)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isResonantBlade(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.IRON_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
