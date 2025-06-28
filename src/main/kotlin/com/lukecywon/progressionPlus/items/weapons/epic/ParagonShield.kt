package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
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

        item.itemMeta = meta
        return applyMeta(item)
    }
}