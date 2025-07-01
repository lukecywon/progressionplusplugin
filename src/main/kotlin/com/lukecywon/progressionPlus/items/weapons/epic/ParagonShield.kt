package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ParagonShield : CustomItem("paragon_shield", Rarity.EPIC) {

    val CONFIG = Config()

    data class Config(
        val aoeWidth: Int = 3,
        val aoeHeight: Int = 2,
        val aoeLength: Int = 5,
        val damage: Double = 12.0,
        val armorShred: Double = -3.0,
        val shredDurationTicks: Long = 100L, // 5 seconds
        val knockbackStrength: Double = 0.5,
        val windupTicks: Long = 10L, // 0.5 seconds
        val cooldownTicks: Long = 200L, // 10 seconds
        val parryWindowTicks: Long = 20L // 1 second
    )

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.SHIELD)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Paragon Shield")
                .color(NamedTextColor.WHITE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Energy Parry", Activation.RIGHT_CLICK),
                ItemLore.description("Perfect parry to reflect energy that shreds armor"),
                ItemLore.cooldown(10),
                ItemLore.separator(),
                ItemLore.lore("The catalyst of energy, which stores and releases power."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun resetModel(item: ItemStack) {
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
    }

    fun isParagonShield(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.SHIELD } ?: false
    }
}