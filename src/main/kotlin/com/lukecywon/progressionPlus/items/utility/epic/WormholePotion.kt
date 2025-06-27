package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.Recipe
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object WormholePotion : CustomItem("wormhole_potion", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.POTION)
        val meta = item.itemMeta

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        meta.displayName(
            Component.text("Wormhole Potion")
                .color(NamedTextColor.LIGHT_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Wormhole Link", Activation.CONSUME),
                ItemLore.description("Drink to teleport to another online player"),
                ItemLore.cooldown(300),
                ItemLore.separator(),
                ItemLore.lore("A potion brewed from stardust and twisted time."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "wormhole_potion")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        // ✨ Add fake enchant for glint
        meta.addEnchant(Enchantment.UNBREAKING, 1, true)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, RecipeChoice.ExactChoice(EchoCore.createItemStack()), null,
            RecipeChoice.MaterialChoice(Material.ENDER_PEARL), RecipeChoice.MaterialChoice(Material.GLASS_BOTTLE), RecipeChoice.MaterialChoice(Material.ENDER_PEARL),
            null, RecipeChoice.MaterialChoice(Material.ENDER_PEARL), null
        )
    }

    fun isWormholePotion(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.POTION) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}
