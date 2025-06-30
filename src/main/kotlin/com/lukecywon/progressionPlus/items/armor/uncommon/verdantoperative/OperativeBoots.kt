package com.lukecywon.progressionPlus.items.armor.uncommon.verdantoperative

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.persistence.PersistentDataType

object OperativeBoots : CustomItem("operative_boots", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.LEATHER_BOOTS)
        item = applyArmor(item, 1.5)
        val meta = item.itemMeta

        meta.displayName(Component.text("Operative Boots", NamedTextColor.GREEN).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Lightweight", Activation.SET_BONUS),
                ItemLore.description("Gain Speed II when wearing the verdant operative gear."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("Standard issue for the forest-bound elite.")
            )
        )

        // Custom trim for wood armor
        if (meta is LeatherArmorMeta && meta is ArmorMeta) {
            meta.setColor(Color.LIME)

            val material = TrimMaterial.COPPER
            val pattern = TrimPattern.SILENCE
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_DYE)
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        // Mark as part of verdant operative set
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "verdant_operative_set"),
            PersistentDataType.BYTE,
            1
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.EMERALD, null, Material.EMERALD,
            Material.EMERALD, null, Material.EMERALD,
            null, null, null
        ))
    }
}