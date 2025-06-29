package com.lukecywon.progressionPlus.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class PendingTeleport(
    val requester: Player,
    val target: Player,
    val expireTime: Long,
    val potionItem: ItemStack
)
