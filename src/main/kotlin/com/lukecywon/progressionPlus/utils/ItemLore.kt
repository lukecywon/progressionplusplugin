package com.lukecywon.progressionPlus.utils

import com.lukecywon.progressionPlus.utils.enums.Activation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.ItemStack

object ItemLore {
    fun abilityuse(text: String, activation: Activation): Component =
        Component.text("Ability: $text", NamedTextColor.GOLD)
            .append(Component.text(" $activation", NamedTextColor.YELLOW))

    fun description(text: String): Component =
        Component.text(text, NamedTextColor.GRAY)

    fun cooldown(seconds: Int): Component {
        val minutes = seconds / 60
        val secs = seconds % 60
        val timeString = when {
            seconds == 0 -> "None"
            minutes > 0 && secs > 0 -> "$minutes min $secs sec"
            minutes > 0 -> "$minutes min"
            else -> "$secs sec"
        }
        return Component.text("Cooldown: $timeString", NamedTextColor.GREEN)
    }

    fun lore(text: String): Component =
        Component.text(text, NamedTextColor.DARK_GRAY).decorate(TextDecoration.ITALIC)

    fun stats(itemStack: ItemStack): Component {
        val type = itemStack.type
        val meta = itemStack.itemMeta ?: return Component.empty()

        val (baseDamage, baseSpeed) = getBaseStats(type)

        // Check for attribute modifiers
        val armorMod = meta.getAttributeModifiers(Attribute.ARMOR)?.sumOf { it.amount } ?: 0.0
        val armorToughnessMod = meta.getAttributeModifiers(Attribute.ARMOR_TOUGHNESS)?.sumOf { it.amount } ?: 0.0
        val damageMod = meta.getAttributeModifiers(Attribute.ATTACK_DAMAGE)?.sumOf { it.amount } ?: 0.0
        val speedMod = meta.getAttributeModifiers(Attribute.ATTACK_SPEED)?.sumOf { it.amount } ?: 0.0

        val isArmor = armorMod != 0.0 || armorToughnessMod != 0.0
        val isWeapon = damageMod != 0.0 || speedMod != 0.0

        return when {
            isArmor -> {
                val displayArmor = armorMod
                val displayToughness = armorToughnessMod
                Component.text(
                    "Stats: %.1f Armor, %.1f Toughness".format(displayArmor, displayToughness),
                    NamedTextColor.AQUA
                )
            }
            isWeapon -> {
                val displayDamage = if (damageMod != 0.0) damageMod else baseDamage
                val displaySpeed = if (speedMod != 0.0) 4 + speedMod else baseSpeed
                Component.text(
                    "Stats: %.1f Damage, %.2f Attack Speed".format(displayDamage, displaySpeed),
                    NamedTextColor.AQUA
                )
            }
            else -> {
                Component.text("Stats: None", NamedTextColor.GRAY)
            }
        }
    }

    fun separator(): Component =
        Component.text("")

    fun getBaseStats(material: Material): Pair<Double, Double> {
        return when (material) {
            // SWORDS
            Material.WOODEN_SWORD     ->  4.0 to 1.6
            Material.STONE_SWORD      ->  5.0 to 1.6
            Material.IRON_SWORD       ->  6.0 to 1.6
            Material.GOLDEN_SWORD     ->  4.0 to 1.6
            Material.DIAMOND_SWORD    ->  7.0 to 1.6
            Material.NETHERITE_SWORD  ->  8.0 to 1.6

            // AXES
            Material.WOODEN_AXE       ->  7.0 to 0.8
            Material.STONE_AXE        ->  9.0 to 0.8
            Material.IRON_AXE         ->  9.0 to 0.9
            Material.GOLDEN_AXE       ->  7.0 to 1.0
            Material.DIAMOND_AXE      ->  9.0 to 1.0
            Material.NETHERITE_AXE    -> 10.0 to 1.0

            // PICKAXES
            Material.WOODEN_PICKAXE   ->  2.0 to 1.2
            Material.STONE_PICKAXE    ->  3.0 to 1.2
            Material.IRON_PICKAXE     ->  4.0 to 1.2
            Material.GOLDEN_PICKAXE   ->  2.0 to 1.2
            Material.DIAMOND_PICKAXE  ->  5.0 to 1.2
            Material.NETHERITE_PICKAXE->  6.0 to 1.2

            // SHOVELS
            Material.WOODEN_SHOVEL    ->  1.5 to 1.0
            Material.STONE_SHOVEL     ->  2.5 to 1.0
            Material.IRON_SHOVEL      ->  3.5 to 1.0
            Material.GOLDEN_SHOVEL    ->  1.5 to 1.0
            Material.DIAMOND_SHOVEL   ->  4.5 to 1.0
            Material.NETHERITE_SHOVEL ->  5.5 to 1.0

            // HOES (Attack Damage differs from others)
            Material.WOODEN_HOE       ->  1.0 to 1.0
            Material.STONE_HOE        ->  1.0 to 2.0
            Material.IRON_HOE         ->  1.0 to 3.0
            Material.GOLDEN_HOE       ->  1.0 to 1.0
            Material.DIAMOND_HOE      ->  1.0 to 4.0
            Material.NETHERITE_HOE    ->  1.0 to 4.0

            Material.TRIDENT -> 8.0 to 1.1

            else -> 0.0 to 0.0
        }
    }
}