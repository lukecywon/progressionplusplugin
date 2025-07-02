package com.lukecywon.progressionPlus.items.armor.rare.paladin

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import com.lukecywon.progressionPlus.utils.enums.Activation
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

object GlassBoots : CustomItem("glass_boots", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.CHAINMAIL_BOOTS)
        item = applyArmor(item, 1.0)
        item = applyArmorToughness(item, 0.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Glass Boots", NamedTextColor.AQUA).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Shatterguard", Activation.SET_BONUS),
                ItemLore.description("Negates 4 heavy hits while wearing the full set."),
                ItemLore.description("Glass armor shatters completely after absorbing damage."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("Fragile but blocks devastating blows.")
            )
        )

        meta.persistentDataContainer.set(NamespacedKey(ProgressionPlus.getPlugin(), "glass_armor"), PersistentDataType.BYTE, 1)

        // Custom trim for glass armor
        if (meta is ArmorMeta) {
            val material = TrimMaterial.DIAMOND
            val pattern = TrimPattern.FLOW
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.GLASS), null, RecipeChoice.MaterialChoice(Material.GLASS),
            RecipeChoice.MaterialChoice(Material.GLASS), null, RecipeChoice.MaterialChoice(Material.GLASS),
            null, null, null
        )
    }
}