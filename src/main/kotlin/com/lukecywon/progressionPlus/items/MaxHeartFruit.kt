package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object MaxHeartFruit : CustomItem("max_heart_fruit", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_APPLE)
        val meta = item.itemMeta

        meta.displayName(Component.text("Max Heart Fruit").color(NamedTextColor.RED))
        meta.lore(
            listOf(
                ItemLore.abilityuse("Heart Bloom", Activation.CONSUME),
                ItemLore.description("Permanently increases max health by 1 heart"),
                ItemLore.description("Cannot be used above 10 hearts"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("The fruit of vitality, blooming only for those at their weakest."),
            )
        )

        meta.setCustomModelData(9020)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isHeartFruit(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.GOLDEN_APPLE) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}