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

object RegenBanner : CustomItem("regen_banner", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.PINK_BANNER)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Regen Banner")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Regeneration", Activation.RIGHT_CLICK),
                ItemLore.description("Grant Regeneration to nearby players for 30s"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("A soothing wave flows forth, mending wounds and weary souls."),
            )
        )

        meta.setCustomModelData(3003)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isRegenBanner(item: ItemStack?): Boolean {
        return item?.type == Material.PINK_BANNER &&
                item.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
    }
}