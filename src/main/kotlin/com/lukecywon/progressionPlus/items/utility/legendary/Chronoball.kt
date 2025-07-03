package com.lukecywon.progressionPlus.items.utility.legendary

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object Chronoball : CustomItem("chronoball", Rarity.LEGENDARY) {

    val CONFIG = Config()

    data class Config(
        val radius: Double = 7.0,
        val durationTicks: Long = 100L, // 5 seconds
        val projectileVelocityMultiplier: Double = 0.1,
        val movementSlow: Double = 0.6, // 60% slow
        val healPercentage: Double = 0.8, // Heal 80% of damage taken
        val cooldownTicks: Long = 300L // 15 seconds
    )

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.SNOWBALL)
        val meta = item.itemMeta!!

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Chronoball")
                .color(NamedTextColor.BLUE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Chronozone", Activation.RIGHT_CLICK),
                ItemLore.description("Creates a time zone around you, slowing enemies and projectiles"),
                ItemLore.abilityuse("Condition Rollback", Activation.RIGHT_CLICK),
                ItemLore.description("If Chronozone is active, end Chronozone and heal a portion of damage taken"),
                ItemLore.cooldown((CONFIG.cooldownTicks / 20).toInt()),
                ItemLore.separator(),
                ItemLore.lore("A hollow, limitless orb of unlimited void."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "chronoball")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isChronoball(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.SNOWBALL } ?: false
    }

    fun resetModel(item: ItemStack) {
        if (!Chronoball .isChronoball(item)) return
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "chronoball")
        item.itemMeta = meta
    }

    fun setActiveModel(item: ItemStack) {
        if (!Chronoball.isChronoball(item)) return
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "chronoball_1")
        item.itemMeta = meta
    }
}
