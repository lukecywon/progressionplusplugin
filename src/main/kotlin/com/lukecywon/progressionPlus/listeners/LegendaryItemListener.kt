package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class LegendaryItemListener : Listener {
    private fun isLegendary(item: ItemStack?): Boolean {
        val rarityKey = NamespacedKey(ProgressionPlus.getPlugin(), "rarity")
        val meta = item?.itemMeta ?: return false
        val container = meta.persistentDataContainer
        return container.get(rarityKey, PersistentDataType.STRING) == "LEGENDARY"
    }

    private fun hasOtherLegendary(player: Player, excluding: ItemStack?): Boolean {
        return player.inventory.contents.any {
            it != null && it != excluding && isLegendary(it)
        }
    }

    fun hasLegendaryItem(player: Player): Boolean {
        return player.inventory.contents.any { isLegendary(it) }
    }

    @EventHandler
    fun onPickup(event: EntityPickupItemEvent) {
        val player = event.entity as? Player ?: return
        val item = event.item.itemStack

        if (isLegendary(item) && hasLegendaryItem(player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val cursor = event.cursor
        val current = event.currentItem

        // Trying to place a legendary item
        if (isLegendary(cursor) && hasLegendaryItem(player)) {
            player.sendMessage(Component.text("❌ You can only carry one Legendary item!", NamedTextColor.RED))
            event.isCancelled = true
        }

        // Trying to swap another item with a legendary
        if (isLegendary(current) && hasOtherLegendary(player, current)) {
            player.sendMessage(Component.text("❌ You already have a Legendary item!", NamedTextColor.RED))
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val player = event.whoClicked as? Player ?: return
        val item = event.oldCursor ?: return

        if (isLegendary(item) && hasOtherLegendary(player, item)) {
            player.sendMessage(Component.text("❌ You already have a Legendary item!", NamedTextColor.RED))
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onCraftItem(event: CraftItemEvent) {
        val player = event.whoClicked as? Player ?: return
        val craftedItem = event.currentItem ?: return

        // Only cancel if the crafted item is Legendary and the player already has one
        if (isLegendary(craftedItem) && hasLegendaryItem(player)) {
            player.sendMessage(Component.text("❌ You already have a Legendary item!", NamedTextColor.RED))
            event.isCancelled = true
        }
    }
}