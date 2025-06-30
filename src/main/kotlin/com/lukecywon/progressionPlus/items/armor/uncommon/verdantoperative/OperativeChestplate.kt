package com.lukecywon.progressionPlus.items.armor.uncommon.verdantoperative

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object OperativeChestplate : CustomItem("operative_chestplate", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.LEATHER_CHESTPLATE)
        item = applyArmor(item, 1.5)
        val meta = item.itemMeta as SkullMeta

        meta.displayName(Component.text("Operative Chestplate", NamedTextColor.GREEN).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Worn by pioneers and forest dwellers,"),
                ItemLore.lore("it creaks with every movement, yet holds with quiet resilience.")
            )
        )

        // Custom trim for wood armor
        if (meta is LeatherArmorMeta && meta is ArmorMeta) {
            meta.setColor(Color.LIME)

            val material = TrimMaterial.EMERALD
            val pattern = TrimPattern.SILENCE
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_DYE)
        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.EMERALD, null, Material.EMERALD,
            Material.EMERALD, Material.VINE, Material.EMERALD,
            Material.EMERALD, Material.EMERALD, Material.EMERALD
        ))
    }
}