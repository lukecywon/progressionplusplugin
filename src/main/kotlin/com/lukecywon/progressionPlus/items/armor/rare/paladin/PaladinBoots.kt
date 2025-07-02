package com.lukecywon.progressionPlus.items.armor.rare.paladin

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
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.persistence.PersistentDataType

object PaladinBoots : CustomItem("paladin_boots", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.IRON_BOOTS)
        item = applyArmor(item, 2.0, EquipmentSlotGroup.FEET)
        item = applyArmorToughness(item, 1.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Paladin Boots", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Light-forged Blessing", Activation.SET_BONUS),
                ItemLore.description("Get 2 extra hearts during the day."),
                ItemLore.separator(),
                ItemLore.abilityuse("A light in the dark", Activation.SET_BONUS),
                ItemLore.description("Push back the darkness and shine like a beacon at night."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("Forged in the hallowed halls of heaven.")
            )
        )

        // Custom trim for wood armor
        if (meta is ArmorMeta) {
            val material = TrimMaterial.GOLD
            val pattern = TrimPattern.EYE
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        // Mark as part of paladin set
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "paladin_set"),
            PersistentDataType.BYTE,
            1
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.GOLD_INGOT), null, RecipeChoice.MaterialChoice(Material.GOLD_INGOT),
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), null, RecipeChoice.ExactChoice(SteelIngot.createItemStack()),
            null, null, null
        )
    }
}