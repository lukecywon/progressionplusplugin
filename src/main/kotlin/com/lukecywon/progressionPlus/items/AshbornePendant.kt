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

object AshbornePendant : CustomItem("ashborne_pendant", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLAZE_POWDER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Ashborne Pendant")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Flame Cone", Activation.RIGHT_CLICK),
                ItemLore.description("Shoots a fiery cone that knocks back and burns foes"),
                ItemLore.description("Knock back is stronger the closer the target"),
                ItemLore.cooldown(15),
                ItemLore.separator(),
                ItemLore.lore("The heat of death lingers in your wake."),
            )
        )

        meta.setCustomModelData(9053)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isAshbornePendant(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.BLAZE_POWDER) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
