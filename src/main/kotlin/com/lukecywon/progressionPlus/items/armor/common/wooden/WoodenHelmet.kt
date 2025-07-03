package com.lukecywon.progressionPlus.items.armor.common.wooden

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.HeadMaker
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object WoodenHelmet : CustomItem("wooden_helmet", Rarity.COMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.LEATHER_HELMET)
        //var item = HeadMaker.createCustomHead("http://textures.minecraft.net/texture/d4dd217ae569605ec023911168ae956412e1616ace2d6c464d42bc6caee0bdcf")
        item = applyArmor(item, 1.5, EquipmentSlotGroup.HEAD)
        val meta = item.itemMeta as SkullMeta

        meta.displayName(Component.text("Wooden Helmet", NamedTextColor.DARK_GREEN).decorate(TextDecoration.BOLD))

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
            meta.setColor(Color.fromRGB(64,103,25))

            val material = TrimMaterial.COPPER
            val pattern = TrimPattern.FLOW
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
            Material.OAK_WOOD, Material.OAK_WOOD, Material.OAK_WOOD,
            Material.OAK_WOOD, null, Material.OAK_WOOD,
            null,null,null
        ))
    }
}