package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object ParagonShield : CustomItem("paragon_shield", Rarity.EPIC) {

    val CONFIG = Config()

    data class Config(
        val aoeWidth: Int = 3,
        val aoeHeight: Int = 2,
        val aoeLength: Int = 5,
        val damage: Double = 12.0,
        val armorShred: Double = -3.0,
        val shredDurationTicks: Long = 100L,
        val knockbackStrength: Double = 0.5,
        val windupTicks: Long = 10L,
        val cooldownTicks: Long = 200L,
        val resistanceDurationTicks: Long = 20L,
        val resistanceAmplifier: Int = 4
    )

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GLOWSTONE_DUST)
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
                ItemLore.cooldown((CONFIG.cooldownTicks / 20).toInt()),
                ItemLore.separator(),
                ItemLore.lore("The catalyst of energy, which stores and releases power.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isParagonShield(item: ItemStack?): Boolean {
        return item?.itemMeta?.displayName()?.equals(Component.text("Paragon Shield").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, true)) == true
    }

    fun resetModel(item: ItemStack) {
        if (!isParagonShield(item)) return
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
    }
}
