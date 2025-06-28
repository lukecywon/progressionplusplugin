package com.lukecywon.progressionPlus.items.weapons.uncommon


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

object BerserkerSword : CustomItem("berserker_sword", Rarity.UNCOMMON) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.STONE_SWORD)
        item = applyBaseDamage(item, 7.0)
        item = applyBaseAttackSpeed(item, 1.6)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Berserker Sword")
                .color(NamedTextColor.DARK_RED)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Adrenaline rush", Activation.RIGHT_CLICK),
                ItemLore.description("Grants the user strength and hunger for 15s"),
                ItemLore.description("Halves the max health of the wielder"),
                ItemLore.cooldown(30),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A reckless edge, fueled by fury and blood."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "berserker_sword")

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isBerserkerSword(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.STONE_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return RecipeGenerator.convertToRecipeChoice(listOf(
            null, Material.REDSTONE, null,
            null, Material.DEEPSLATE, null,
            null, Material.STICK, null
        ))
    }
}