package com.lukecywon.progressionPlus.items.armor.rare.paladin

import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.HeadMaker
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice

object PaladinHelmet : CustomItem("paladin_helmet", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = HeadMaker.createCustomHead("http://textures.minecraft.net/texture/60fd2cc9116c0f724bdcdbf2e9633cb0a7d453f8b3a1ad9d1493e5e6f1281555")
        item = applyArmor(item, 2.0)
        item = applyArmorToughness(item, 1.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Paladin Helmet", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Forged in the hallowed halls of heaven.")
            )
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), RecipeChoice.MaterialChoice(Material.GOLD_INGOT), RecipeChoice.ExactChoice(SteelIngot.createItemStack()),
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), null, RecipeChoice.ExactChoice(SteelIngot.createItemStack()),
            null,null,null
        )
    }
}