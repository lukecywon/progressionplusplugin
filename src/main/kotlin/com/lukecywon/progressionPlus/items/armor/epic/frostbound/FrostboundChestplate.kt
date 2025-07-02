package com.lukecywon.progressionPlus.items.armor.epic.frostbound

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.items.component.TideCrystal
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

object FrostboundChestplate : CustomItem("frostbound_chestplate", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.DIAMOND_CHESTPLATE)
        item = applyArmor(item, 7.5, EquipmentSlotGroup.CHEST)
        item = applyArmorToughness(item, 1.5)
        val meta = item.itemMeta

        meta.displayName(Component.text("Frostbound Chestplate", NamedTextColor.AQUA).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Frostbite Retaliation", Activation.SET_BONUS),
                ItemLore.description("Attackers are afflicted with Frostbite on impact."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("The cold bites back harder than the blade.")
            )
        )

        // Custom trim for enderite
        if (meta is ArmorMeta) {
            val material = TrimMaterial.QUARTZ
            val pattern = TrimPattern.FLOW
            val trim = ArmorTrim(material, pattern)

            meta.trim = trim
        }

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

        // Mark as part of paladin set
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "frostbound_set"),
            PersistentDataType.BYTE,
            1
        )

        item.itemMeta = meta
        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.PACKED_ICE), null, RecipeChoice.MaterialChoice(Material.PACKED_ICE),
            RecipeChoice.MaterialChoice(Material.BLUE_ICE), RecipeChoice.MaterialChoice(Material.DIAMOND_CHESTPLATE), RecipeChoice.MaterialChoice(Material.BLUE_ICE),
            RecipeChoice.MaterialChoice(Material.BLUE_ICE), RecipeChoice.ExactChoice(TideCrystal.createItemStack()), RecipeChoice.MaterialChoice(Material.BLUE_ICE)
            )
    }
}