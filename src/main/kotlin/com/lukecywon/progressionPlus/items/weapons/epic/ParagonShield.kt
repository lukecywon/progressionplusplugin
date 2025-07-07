package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.items.component.TideCrystal
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object ParagonShield : CustomItem("paragon_shield", Rarity.EPIC) {

    val CONFIG = Config()

    data class Config(
        val aoeWidth: Int = 2,
        val aoeHeight: Int = 2,
        val aoeLength: Int = 5,
        val damage: Double = 7.0,
        val armorShred: Double = -4.0,
        val shredDurationTicks: Long = 120L,
        val knockbackStrength: Double = 1.0,
        val windupTicks: Long = 10L,
        val cooldownMillis: Long = 15_000L,
        val parryWindowTicks: Long = 20L,
        val resistanceLevel: Int = 3
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
                ItemLore.cooldown((CONFIG.cooldownMillis / 1000).toInt()),
                ItemLore.separator(),
                ItemLore.lore("The catalyst of energy, which stores and releases power.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isParagonShield(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.GLOWSTONE_DUST } ?: false
    }

    fun resetModel(item: ItemStack) {
        if (!isParagonShield(item)) return
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield")
        item.itemMeta = meta
    }

    fun setActiveModel(item: ItemStack) {
        if (!isParagonShield(item)) return
        val meta = item.itemMeta ?: return
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "paragon_shield_1")
        item.itemMeta = meta
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.BLUE_ICE), RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.MaterialChoice(Material.BLUE_ICE),
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), RecipeChoice.ExactChoice(TideCrystal.createItemStack()), RecipeChoice.ExactChoice(SteelIngot.createItemStack()),
            null, RecipeChoice.ExactChoice(SteelIngot.createItemStack()), null
        )
    }
}
