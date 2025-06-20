package com.lukecywon.progressionPlus.items.weapons.rare

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object Peacemaker : CustomItem("peacemaker", Rarity.RARE) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.IRON_HOE)
        item = applyBaseDamage(item, 0.0)
        item = applyBaseAttackSpeed(item, 1.0)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Peacemaker")
                .color(NamedTextColor.WHITE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Bullet Load", Activation.SHIFT_RIGHT_CLICK),
                ItemLore.description("Load a bullet into the chamber"),
                ItemLore.abilityuse("Shoot", Activation.RIGHT_CLICK),
                ItemLore.description("Fire a bullet with slight spread"),
                ItemLore.abilityuse("Fan the Hammer", Activation.LEFT_CLICK),
                ItemLore.description("Unload all bullets rapidly in a wide arc"),
                ItemLore.cooldown(2),
                Component.text(
                    "Stats: 6.0 Damage",
                    NamedTextColor.AQUA
                ),
                ItemLore.separator(),
                ItemLore.lore("It's high noon."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "peacemaker")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            Material.IRON_INGOT, Material.IRON_INGOT, Material.TRIPWIRE_HOOK,
            Material.BLAZE_ROD, Material.GUNPOWDER, Material.CROSSBOW,
            null, null, Material.STICK
        ))
    }
}
