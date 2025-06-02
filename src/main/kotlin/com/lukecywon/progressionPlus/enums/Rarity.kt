package com.lukecywon.progressionPlus.enums

import net.kyori.adventure.text.format.NamedTextColor

enum class Rarity(val displayName: String, val color: NamedTextColor) {
    COMMON("COMMON", NamedTextColor.WHITE),
    UNCOMMON("UNCOMMON", NamedTextColor.GREEN),
    RARE("RARE", NamedTextColor.BLUE),
    EPIC("EPIC", NamedTextColor.DARK_PURPLE),
    LEGENDARY("LEGENDARY", NamedTextColor.GOLD)
}