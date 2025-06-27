package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
import com.lukecywon.progressionPlus.items.component.TwistedRoot
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object BuilderWand : CustomItem("builder_wand", Rarity.EPIC) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLAZE_ROD)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Builder’s Wand")
                .color(NamedTextColor.GOLD)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Mass Place", Activation.RIGHT_CLICK),
                ItemLore.description("Selects and clones connected blocks"),
                ItemLore.description("forward up to 64 blocks max."),
                ItemLore.description("Uses blocks from your inventory."),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A wand for builders with vision.")
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "builder_wand")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isBuilderWand(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.BLAZE_ROD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, null, RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()),
            null, RecipeChoice.MaterialChoice(Material.EMERALD_BLOCK), null,
            RecipeChoice.ExactChoice(TwistedRoot.createItemStack()), null, null
        )
    }
}
