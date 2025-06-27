package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object FamesAuri : CustomItem("fames_auri", Rarity.LEGENDARY) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_SWORD)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Fames Auri")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Goldly Blessings", Activation.RIGHT_CLICK),
                ItemLore.description("Toggle to draw golden power at the cost of golden treasure"),
                ItemLore.abilityuse("Sacrifice Switch", Activation.SHIFT_RIGHT_CLICK),
                ItemLore.description("Change between sacrifice tiers - nuggets, ingots or blocks"),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Aurum invocat. Tu obedis."),
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    data class BuffConfig(
        val material: Material,
        val label: String,
        val activationDelayTicks: Long,
        val intervalTicks: Long,
        val potionEffects: List<PotionEffect> = listOf(),
        val attributeModifiers: List<Pair<Attribute, AttributeModifier>> = listOf()
    )

    val buffs = listOf(
        BuffConfig(
            material = Material.GOLD_NUGGET,
            label = "Tier I",
            activationDelayTicks = 40L,
            intervalTicks = 60L,
            potionEffects = listOf(
                PotionEffect(PotionEffectType.SPEED, 60, 0, true, false),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, true, false),
            ) ,
            attributeModifiers = listOf(
                Attribute.ATTACK_DAMAGE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_damage".toByteArray()), "fames_auri_damage", 2.0, AttributeModifier.Operation.ADD_NUMBER),
            )
        ),
        BuffConfig(
            material = Material.GOLD_INGOT,
            label = "Tier II",
            activationDelayTicks = 60L,
            intervalTicks = 40L,
            potionEffects = listOf(
                PotionEffect(PotionEffectType.SPEED, 40, 1, true, false),
                PotionEffect(PotionEffectType.SATURATION, 40, 0, true, false),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 40, 0, true, false),
                ),
            attributeModifiers = listOf(
                Attribute.ATTACK_DAMAGE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_damage".toByteArray()), "fames_auri_damage", 4.0, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.KNOCKBACK_RESISTANCE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_knockback".toByteArray()), "fames_auri_knockback", 0.15, AttributeModifier.Operation.ADD_NUMBER),
                )
        ),
        BuffConfig(
            material = Material.GOLD_BLOCK,
            label = "Tier III",
            activationDelayTicks = 100L,
            intervalTicks = 10L,
            potionEffects = listOf(
                PotionEffect(PotionEffectType.SPEED, 10, 2, true, false),
                PotionEffect(PotionEffectType.SATURATION, 10, 1, true, false),
                PotionEffect(PotionEffectType.ABSORPTION, 10, 0, true, false),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 10, 0, true, false),
                PotionEffect(PotionEffectType.WATER_BREATHING, 10, 0, true, false),
            ),
            attributeModifiers = listOf(
                Attribute.ATTACK_DAMAGE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_damage".toByteArray()), "fames_auri_damage", 7.0, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.KNOCKBACK_RESISTANCE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_knockback".toByteArray()), "fames_auri_knockback", 0.6, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.ATTACK_SPEED to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_speed".toByteArray()), "fames_auri_speed", 0.5, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.FALL_DAMAGE_MULTIPLIER to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_nofall".toByteArray()), "fames_auri_nofall", -1.0, AttributeModifier.Operation.ADD_NUMBER),
            )
        )
    )

    fun isFamesAuri(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.GOLDEN_SWORD } ?: false
    }
}
