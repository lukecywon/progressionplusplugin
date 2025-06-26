package com.lukecywon.progressionPlus.items.weapons.epic

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
import com.lukecywon.progressionPlus.items.component.WardensHeart
import net.kyori.adventure.text.Component
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object ShadowKatana : CustomItem("shadow_katana", Rarity.EPIC) {
    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.NETHERITE_SWORD)
        item = applyBaseDamage(item, 11.0)
        item = applyBaseAttackSpeed(item)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Shadow Katana")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

//        meta.displayName(
//            Component.text("影の刀")
//                .color(NamedTextColor.DARK_PURPLE)
//                .decorate(TextDecoration.BOLD)
//        )

//        meta.lore(
//            listOf(
//                ItemLore.abilityuse("シャドウダッシュ", Activation.RIGHT_CLICK),
//                ItemLore.description("前方に素早くダッシュし、敵を斬り抜ける"),
//                ItemLore.description("5秒間、斬撃の軌跡を残す"),
//                ItemLore.cooldown(15),
//                ItemLore.stats(item),
//                ItemLore.separator(),
//                ItemLore.lore("目にも留まらぬ速さで斬り裂く。")
//            )
//        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Shadow Dash", Activation.RIGHT_CLICK),
                ItemLore.description("Dash forward and slash through enemies"),
                ItemLore.description("Leaves a slash trail for 5s"),
                ItemLore.cooldown(15),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Slashes faster than the eye can see."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "shadow_katana")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun isShadowKatana(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.NETHERITE_SWORD) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            null, RecipeChoice.MaterialChoice(Material.NETHERITE_SCRAP), RecipeChoice.ExactChoice(
                EchoCore.createItemStack()),
            RecipeChoice.MaterialChoice(Material.NETHERITE_SCRAP), RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.MaterialChoice(Material.NETHERITE_SCRAP),
            RecipeChoice.MaterialChoice(Material.NETHERITE_SWORD), RecipeChoice.MaterialChoice(Material.NETHERITE_SCRAP), null
        )
    }
}
