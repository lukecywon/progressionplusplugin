package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.InfernalShard
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object PhoenixTotem : CustomItem("phoenix_totem", Rarity.EPIC) {
    val phoenixKey = NamespacedKey(ProgressionPlus.instance, "phoenix_totem")

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.TOTEM_OF_UNDYING)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Phoenix Totem")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Rebirth", Activation.DEATH),
                ItemLore.description("Prevents death and brings you to your spawn"),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("When all seems lost, it saves you in a blaze of fire."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "phoenix_totem")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, RecipeChoice.MaterialChoice(Material.TOTEM_OF_UNDYING), null,
            RecipeChoice.MaterialChoice(Material.TOTEM_OF_UNDYING), RecipeChoice.ExactChoice(InfernalShard.createItemStack()), RecipeChoice.MaterialChoice(Material.TOTEM_OF_UNDYING),
            null, RecipeChoice.MaterialChoice(Material.TOTEM_OF_UNDYING), null
        )
    }
}

