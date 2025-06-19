package com.lukecywon.progressionPlus.items.weapons.rare

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ExecutionerSword : CustomItem("executioner_sword", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.IRON_SWORD)
        val meta = item.itemMeta!!

        meta.displayName(Component.text("Executioner Sword").color(Rarity.RARE.color))
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        meta.lore(listOf(
            ItemLore.abilityuse("Final Verdict", Activation.RIGHT_CLICK),
            ItemLore.description("Cleaves all enemies in front of you"),
            ItemLore.cooldown(0),
            ItemLore.stats(item),
            ItemLore.separator(),
            ItemLore.lore("The final sight of many men."),
        ))

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }
}