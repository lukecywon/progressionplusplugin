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
        val potionEffects: List<PotionEffect> = listOf(),
        val attributeModifiers: List<Pair<Attribute, AttributeModifier>> = listOf(),
        val label: String
    )

    val buffs = listOf(
        BuffConfig(
            Material.GOLD_NUGGET,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 0)),
            label = "Speed I"
        ),
        BuffConfig(
            Material.GOLD_INGOT,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 1)),
            label = "Speed II"
        ),
        BuffConfig(
            Material.GOLD_BLOCK,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 3)),
            label = "Speed IV"
        )
    )



    fun isFamesAuri(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.GOLDEN_SWORD } ?: false
    }
}
