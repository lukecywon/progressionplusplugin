package com.lukecywon.progressionPlus.items.armor.epic.enderite

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
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

object EnderiteChestplate : CustomItem("enderite_chestplate", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.NETHERITE_CHESTPLATE)
        item = applyArmor(item, 8.5, EquipmentSlotGroup.CHEST)
        item = applyArmorToughness(item, 3.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Enderite Chestplate", NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Endborn Grace", Activation.SET_BONUS),
                ItemLore.description("Gain Speed II and an extra 2 ❤ while wearing the full set."),
                ItemLore.separator(),
                ItemLore.abilityuse("Voidwalker's Dodge", Activation.SET_BONUS),
                ItemLore.description("Dodge every 5h hit taken."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("Movement feels weightless, as if space bends to your will.")
            )
        )

        // Custom trim for enderite
        if (meta is ArmorMeta) {
            val material = TrimMaterial.AMETHYST
            val pattern = TrimPattern.SILENCE
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        // Mark as part of paladin set
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "enderite_set"),
            PersistentDataType.BYTE,
            1
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), RecipeChoice.MaterialChoice(Material.GOLD_INGOT), RecipeChoice.ExactChoice(
                SteelIngot.createItemStack()),
            RecipeChoice.ExactChoice(SteelIngot.createItemStack()), null, RecipeChoice.ExactChoice(SteelIngot.createItemStack()),
            null,null,null
        )
    }
}