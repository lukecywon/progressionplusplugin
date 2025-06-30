package com.lukecywon.progressionPlus.items.armor.uncommon.verdantoperative

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.persistence.PersistentDataType

object OperativeHeadgear : CustomItem("operative_headgear", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.TURTLE_HELMET)
        item = applyArmor(item, 2.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Operative Headgear", NamedTextColor.GREEN).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Worn by pioneers and forest dwellers,"),
                ItemLore.lore("it creaks with every movement, yet holds with quiet resilience.")
            )
        )

        // Custom trim for wood armor
        if (meta is ArmorMeta) {
            val material = TrimMaterial.NETHERITE
            val pattern = TrimPattern.COAST
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
            Material.EMERALD, Material.EMERALD, Material.EMERALD,
            Material.VINE, null, Material.VINE,
            null, null, null
        ))
    }
}