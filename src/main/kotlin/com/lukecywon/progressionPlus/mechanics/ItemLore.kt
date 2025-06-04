package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

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

    fun separator(): Component =
        Component.text("")
}