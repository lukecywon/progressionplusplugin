package com.lukecywon.progressionPlus.items.armor.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.InfernalShard
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object RocketHarness : CustomItem("rocket_harness", Rarity.EPIC, false) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.RED_HARNESS)
        val meta = item.itemMeta

        meta.displayName(Component.text("Rocket Harness").color(NamedTextColor.RED).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Rocket Fueled Speed", Activation.PASSIVE),
                ItemLore.description("Doubles the fly speed of a Happy Ghast"),
                ItemLore.separator(),
                ItemLore.lore("A harness with a rocket strapped onto it's back."),
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.REDSTONE), RecipeChoice.MaterialChoice(Material.RED_HARNESS), RecipeChoice.MaterialChoice(Material.REDSTONE),
            RecipeChoice.MaterialChoice(Material.REDSTONE), RecipeChoice.ExactChoice(InfernalShard.createItemStack()), RecipeChoice.MaterialChoice(Material.REDSTONE),
            RecipeChoice.MaterialChoice(Material.REDSTONE), RecipeChoice.MaterialChoice(Material.REDSTONE), RecipeChoice.MaterialChoice(Material.REDSTONE)
        )
    }
}