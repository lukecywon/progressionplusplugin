package com.lukecywon.progressionPlus.listeners

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CraftRestrictListener : Listener {

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

    private val requiredAdvancement = NamespacedKey.minecraft("end/root")

    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        val player = e.view.player as? Player ?: return
        val result = e.inventory.result ?: return
        if (result.type !in restrictedItems) return

        val advancement = Bukkit.getAdvancement(requiredAdvancement) ?: return
        val progress = player.getAdvancementProgress(advancement)

        if (!progress.isDone) {
            e.inventory.result = ItemStack(Material.AIR)
        }
    }
}
