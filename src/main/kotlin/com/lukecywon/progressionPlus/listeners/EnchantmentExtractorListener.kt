package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.utility.uncommon.EnchantmentExtractor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class EnchantmentExtractorListener : Listener {

    @EventHandler
    fun onUse(e: PlayerInteractEvent) {
        // Only handle main hand to avoid duplicate event calls
        if (e.hand != EquipmentSlot.HAND) return
        if (!e.action.name.contains("RIGHT_CLICK")) return

        val player = e.player
        val mainHand = player.inventory.itemInMainHand
        val offHand = player.inventory.itemInOffHand

        if (!EnchantmentExtractor.isThisItem(offHand)) return

        // Check if there's anything to extract
        if (mainHand.type == Material.AIR || mainHand.enchantments.isEmpty()) {
            player.sendMessage("§cNo enchantments to extract.")
            return
        }

        // Create a new enchanted book with all stored enchantments
        val enchantedBook = ItemStack(Material.ENCHANTED_BOOK)
        val bookMeta = enchantedBook.itemMeta as? EnchantmentStorageMeta ?: return

        for ((enchant, level) in mainHand.enchantments) {
            bookMeta.addStoredEnchant(enchant, level, false)
        }

        enchantedBook.itemMeta = bookMeta

        // Replace item in main hand with the enchanted book
        player.inventory.setItemInMainHand(enchantedBook)

        // Consume the extractor
        offHand.amount = offHand.amount - 1

        player.sendMessage("§aAll enchantments have been successfully extracted.")
        e.isCancelled = true
    }
}
