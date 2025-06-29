package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
import com.lukecywon.progressionPlus.utils.ItemLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object SoulrendScythe : CustomItem("soulrend_scythe", Rarity.EPIC, true) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.IRON_SWORD)
        item = applyBaseDamage(item, 9.0)
        item = applyBaseAttackSpeed(item, 1.8)
        val meta = item.itemMeta!!

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Soulrend Scythe")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Lifesteal", Activation.PASSIVE),
                ItemLore.description("Deals +1 damage per debuff on you"),
                ItemLore.description("Reduced boost from bad omen"),
                ItemLore.description("Restores 25% of damage dealt as health"),
                ItemLore.cooldown(0),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("The more you suffer, the deeper it carves."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "soulrend_scythe")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.MaterialChoice(Material.OMINOUS_BOTTLE),
            null, RecipeChoice.MaterialChoice(Material.DIAMOND_HOE), null,
            RecipeChoice.MaterialChoice(Material.BLAZE_ROD), null, null
        )
    }
}
