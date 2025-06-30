package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.InfernalShard
import com.lukecywon.progressionPlus.items.component.TwistedRoot
import com.lukecywon.progressionPlus.items.weapons.rare.ResonantBlade
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object FerociousBlade : CustomItem("ferocious_blade", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.STONE_SWORD)
        item = applyBaseDamage(item, 7.0)
        item = applyBaseAttackSpeed(item, 1.4)
        val meta = item.itemMeta!!

        // Set display name and lore
        meta.displayName(
            Component.text("Ferocious Blade")
                .color(NamedTextColor.RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Relentless Blow", Activation.HIT),
                ItemLore.description("50% chance to strike a second time on hit"),
                ItemLore.cooldown(0),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Strikes twice when fate allows, always hungry for more."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "ferocious_blade")
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, RecipeChoice.ExactChoice(InfernalShard.createItemStack()), null,
            null, RecipeChoice.ExactChoice(ResonantBlade.createItemStack()), null,
            null, RecipeChoice.ExactChoice(TwistedRoot.createItemStack()), null
        )
    }
}