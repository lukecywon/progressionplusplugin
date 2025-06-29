package com.lukecywon.progressionPlus.utils.enums

import net.kyori.adventure.text.format.NamedTextColor

enum class Rarity(val displayName: String, val color: NamedTextColor) {
    COMMON("COMMON", NamedTextColor.WHITE),
    UNCOMMON("UNCOMMON", NamedTextColor.GREEN),
    RARE("RARE", NamedTextColor.BLUE),
    EPIC("EPIC", NamedTextColor.DARK_PURPLE),
    LEGENDARY("LEGENDARY", NamedTextColor.GOLD),
    COMPONENT("COMPONENT", NamedTextColor.GRAY),
    PROGRESSION("PROGRESSION", NamedTextColor.DARK_AQUA)
}