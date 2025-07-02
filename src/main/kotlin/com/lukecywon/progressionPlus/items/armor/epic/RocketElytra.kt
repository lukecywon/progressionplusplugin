package com.lukecywon.progressionPlus.items.armor.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.AetherCore
import com.lukecywon.progressionPlus.items.component.InfernalShard
import com.lukecywon.progressionPlus.utils.ItemLore
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object RocketElytra : CustomItem("rocket_elytra", Rarity.EPIC, enchantable = false) {
    val rocketKey = NamespacedKey(ProgressionPlus.getPlugin(), "rocket_elytra")

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.ELYTRA)
        item = applyArmor(item, 6.0, EquipmentSlotGroup.CHEST)
        item = applyArmorToughness(item, 1.0)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Rocket Elytra")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Blast Off", Activation.DEPLOY),
                ItemLore.description("Launches you upward when you begin gliding."),
                ItemLore.cooldown(0),
                ItemLore.abilityuse("Rocket Boost", Activation.SNEAK),
                ItemLore.description("Consumes 1 charge to dash midair."),
                ItemLore.description("Stores up to 3 charges"),
                ItemLore.cooldown(10),
                ItemLore.separator(),
                ItemLore.stats(item),
                ItemLore.lore("The skies call, and rockets answer.")
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "rocket_elytra")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.persistentDataContainer.set(rocketKey, PersistentDataType.BYTE, 1)

        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        item.itemMeta = meta

        item.addUnsafeEnchantment(Enchantment.UNBREAKING, 1) // Fake enchantment to show glint
        return applyMeta(item)
    }


    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, RecipeChoice.MaterialChoice(Material.FIREWORK_ROCKET), null,
            RecipeChoice.ExactChoice(InfernalShard.createItemStack()), RecipeChoice.ExactChoice(
                AetherCore.createItemStack()), RecipeChoice.ExactChoice(
                InfernalShard.createItemStack()),
            null, RecipeChoice.MaterialChoice(Material.FIREWORK_ROCKET), null
        )
    }
}