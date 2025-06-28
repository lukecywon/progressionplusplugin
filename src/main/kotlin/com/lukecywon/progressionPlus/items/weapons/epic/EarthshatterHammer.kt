package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SunscorchedEmber
import com.lukecywon.progressionPlus.items.weapons.rare.AshenWarhammer
import com.lukecywon.progressionPlus.mechanics.ItemLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object EarthshatterHammer : CustomItem("earthshatter_hammer", Rarity.EPIC, true) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.NETHERITE_AXE)
        item = applyBaseDamage(item, 10.0)
        item = applyBaseAttackSpeed(item, 0.9)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Earthshatter Hammer")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )
        meta.lore(
            listOf(
                ItemLore.abilityuse("Earthshatter", Activation.RIGHT_CLICK),
                ItemLore.description("Launch blocks upward in a shockwave"),
                ItemLore.cooldown(30),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A mighty blow that shakes the land.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "earthshatter_hammer")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.NETHER_STAR), RecipeChoice.MaterialChoice(Material.ANVIL), RecipeChoice.MaterialChoice(Material.PIGLIN_HEAD),
            null, RecipeChoice.ExactChoice(AshenWarhammer.createItemStack()), null,
            null, RecipeChoice.MaterialChoice(Material.STICK), null
        )
    }
}
