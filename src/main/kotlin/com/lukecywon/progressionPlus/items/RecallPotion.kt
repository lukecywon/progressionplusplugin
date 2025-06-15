package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object RecallPotion : CustomItem("recall_potion", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.POTION)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Recall Potion")
                .color(NamedTextColor.AQUA)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Recall", Activation.CONSUME),
                ItemLore.description("Drink to teleport to your spawn after 5s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("No matter how far, its magic finds the way home."),
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isRecallPotion(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.POTION) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
