package com.lukecywon.progressionPlus.items.armor.rare.paladin

import com.lukecywon.progressionPlus.ProgressionPlus
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
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType


object GlassHelmet : CustomItem("glass_helmet", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = HeadMaker.createCustomHead("https://static.planetminecraft.com/files/resource_media/skin/1232/sketch15_3165585.png")
        item = applyArmor(item, 2.0)
        item = applyArmorToughness(item, 0.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Glass Helmet", NamedTextColor.AQUA).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Fragile but blocks devastating blows."),
                ItemLore.lore("Shatters after 4 heavy hits.")
            )
        )

        meta.persistentDataContainer.set(NamespacedKey(ProgressionPlus.getPlugin(), "glass_armor"), PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.MaterialChoice(Material.GLASS), RecipeChoice.MaterialChoice(Material.GLASS),
            RecipeChoice.MaterialChoice(Material.GLASS), null, RecipeChoice.MaterialChoice(Material.GLASS),
            null, null, null
        )
    }
}