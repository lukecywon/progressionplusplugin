package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.items.weapons.epic.EarthshatterHammer
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object FamesAuri : CustomItem("fames_auri", Rarity.LEGENDARY, enchantable = false) {

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.GOLDEN_SWORD)

        item = applyBaseDamage(item, 4.0)
        item = applyBaseAttackSpeed(item, 1.8)
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

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "fames_auri")
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
            intervalTicks = 8L,
            potionEffects = listOf(
                PotionEffect(PotionEffectType.SPEED, 8, 2, true, false),
                PotionEffect(PotionEffectType.ABSORPTION, 8, 0, true, false),
                PotionEffect(PotionEffectType.FIRE_RESISTANCE, 8, 0, true, false),
                PotionEffect(PotionEffectType.WATER_BREATHING, 8, 0, true, false),
            ),
            attributeModifiers = listOf(
                Attribute.ATTACK_DAMAGE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_damage".toByteArray()), "fames_auri_damage", 7.0, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.KNOCKBACK_RESISTANCE to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_knockback".toByteArray()), "fames_auri_knockback", 0.6, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.ATTACK_SPEED to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_speed".toByteArray()), "fames_auri_speed", 0.5, AttributeModifier.Operation.ADD_NUMBER),
                Attribute.FALL_DAMAGE_MULTIPLIER to AttributeModifier(UUID.nameUUIDFromBytes("fames_auri_nofall".toByteArray()), "fames_auri_nofall", -1.0, AttributeModifier.Operation.ADD_NUMBER),
            )
        )
    )

    fun applyTierModel(item: ItemStack, tier: Int) {
        val meta = item.itemMeta ?: return
        val modelKey = NamespacedKey(NamespacedKey.MINECRAFT, when (tier) {
            0 -> "fames_auri_1"
            1 -> "fames_auri_2"
            2 -> "fames_auri_3"
            else -> "fames_auri"
        })
        meta.itemModel = modelKey
        item.itemMeta = meta
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.GOLD_BLOCK), RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.MaterialChoice(Material.GOLD_BLOCK),
            RecipeChoice.MaterialChoice(Material.ENCHANTED_GOLDEN_APPLE), RecipeChoice.MaterialChoice(Material.NETHERITE_SWORD), RecipeChoice.MaterialChoice(Material.ENCHANTED_GOLDEN_APPLE),
            RecipeChoice.MaterialChoice(Material.GOLD_BLOCK), RecipeChoice.MaterialChoice(Material.END_ROD), RecipeChoice.MaterialChoice(Material.GOLD_BLOCK)
        )
    }
}
