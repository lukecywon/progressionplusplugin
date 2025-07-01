package com.lukecywon.progressionPlus.items.armor.epic.enderite

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.SteelIngot
import com.lukecywon.progressionPlus.utils.HeadMaker
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
import org.bukkit.persistence.PersistentDataType

object EnderiteHelmet : CustomItem("enderite_helmet", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        var item = HeadMaker.createCustomHead("http://textures.minecraft.net/texture/c40e8962143ef41defb0b6489d736b4fa88a9387f8cde0c5f366928687912144")
        item = applyArmor(item, 4.0, EquipmentSlotGroup.HEAD)
        item = applyArmorToughness(item, 3.0)
        val meta = item.itemMeta

        meta.displayName(Component.text("Enderite Helmet", NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD))

        meta.lore(
            listOf(
                ItemLore.abilityuse("Endborn Grace", Activation.SET_BONUS),
                ItemLore.description("Gain Speed II and an extra 2 ❤ while wearing the full set."),
                ItemLore.separator(),
                ItemLore.abilityuse("Voidwalker's Dodge", Activation.SET_BONUS),
                ItemLore.description("Have a 20% chance to dodge attacks when taking damage."),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("Movement feels weightless, as if space bends to your will.")
            )
        )

        // Mark as part of paladin set
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "enderite_set"),
            PersistentDataType.BYTE,
            1
        )

        meta.addItemFlags(ItemFlag.HIDE_ARMOR_TRIM)

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