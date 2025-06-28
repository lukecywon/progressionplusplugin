package com.lukecywon.progressionPlus.items.utility.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
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
import java.util.*

object AbyssalBox : CustomItem("abyssal_box", Rarity.EPIC) {

    val boxIdKey = NamespacedKey(ProgressionPlus.getPlugin(), "abyssal_box_id")

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BLACK_SHULKER_BOX)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Abyssal Box")
                .color(NamedTextColor.DARK_PURPLE)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Void Storage", Activation.RIGHT_CLICK),
                ItemLore.description("Opens a small container capable of"),
                ItemLore.description("holding even shulker boxes within."),
                ItemLore.cooldown(0),
                ItemLore.separator(),
                ItemLore.lore("A void-bound vault, unburdened by nesting laws.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "abyssal_box")
        // Assign unique ID for storage
        if (!meta.persistentDataContainer.has(boxIdKey, PersistentDataType.STRING)) {
            meta.persistentDataContainer.set(boxIdKey, PersistentDataType.STRING, UUID.randomUUID().toString())
        }

        item.itemMeta = meta
        return applyMeta(item)
    }
    fun getBoxId(item: ItemStack?): String? {
        return item?.itemMeta?.persistentDataContainer?.get(boxIdKey, PersistentDataType.STRING)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.ENDERMITE_SPAWN_EGG), RecipeChoice.MaterialChoice(Material.SHULKER_BOX), RecipeChoice.MaterialChoice(Material.ENDERMITE_SPAWN_EGG),
            RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()),
            RecipeChoice.MaterialChoice(Material.ENDERMITE_SPAWN_EGG), RecipeChoice.MaterialChoice(Material.SHULKER_BOX), RecipeChoice.MaterialChoice(Material.ENDERMITE_SPAWN_EGG)
        )
    }
}
