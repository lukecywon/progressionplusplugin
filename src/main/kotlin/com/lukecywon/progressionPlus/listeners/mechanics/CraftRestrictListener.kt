package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player

class CraftRestrictListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()

    private val restrictedItems = setOf(
        Material.DIAMOND_SWORD,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_AXE,
        Material.DIAMOND_SHOVEL,
        Material.DIAMOND_HOE,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS
    )

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        val player = e.view.player as? Player ?: return
        val result = e.inventory.result ?: return
        if (result.type !in restrictedItems) return

        if (!plugin.config.getBoolean("diamond_unlocked")) {
            e.inventory.result = ItemStack(Material.AIR)
        }
    }
}
