package com.lukecywon.progressionPlus.listeners.utility

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItemRegistry
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CustomItemVanillaBlocker : Listener {

    private fun isCustomItem(item: ItemStack?): Boolean {
        if (item == null || item.type == Material.AIR) return false
        return CustomItemRegistry.getAll().any { it.isThisItem(item) }
    }

    // 🚫 Prevent using custom items to craft vanilla results
    @EventHandler
    fun onCraft(e: PrepareItemCraftEvent) {
        val matrix = e.inventory.matrix
        val hasCustomItem = matrix.any { isCustomItem(it) }
        val output = e.inventory.result
        val isCustomOutput = isCustomItem(output)

        if (hasCustomItem && !isCustomOutput) {
            e.inventory.result = null
        }
    }

    // 🚫 Prevent using custom items as furnace fuel
    @EventHandler
    fun onFurnaceFuel(e: FurnaceBurnEvent) {
        if (isCustomItem(e.fuel)) {
            e.isCancelled = true
        }
    }

    // 🚫 Prevent using custom items in brewing stand
    @EventHandler
    fun onBrew(e: BrewEvent) {
        val ingredient = e.contents.ingredient
        if (isCustomItem(ingredient)) {
            e.isCancelled = true
        }
    }

    // 🚫 Prevent using custom items in smithing
    @EventHandler
    fun onSmith(e: PrepareSmithingEvent) {
        val inputs = listOf(e.inventory.getItem(0), e.inventory.getItem(1))
        if (inputs.any { isCustomItem(it) }) {
            e.result = null
        }
    }

    // 🚫 Prevent grindstone use
    @EventHandler
    fun onGrindstone(e: PrepareGrindstoneEvent) {
        val inputs = listOf(e.inventory.getItem(0), e.inventory.getItem(1))
        if (inputs.any { isCustomItem(it) }) {
            e.result = null
        }
    }

    // 🚫 Prevent composter input
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory.type == InventoryType.COMPOSTER && isCustomItem(e.cursor)) {
            e.isCancelled = true
        }
    }

    // 🚫 Prevent anvil interactions
    @EventHandler
    fun onAnvil(e: PrepareAnvilEvent) {
        val left = e.inventory.getItem(0)
        val right = e.inventory.getItem(1)

        val leftIsEnchantable = left?.persistentDataContainer?.get(NamespacedKey(
            ProgressionPlus.getPlugin(),
            "enchantable"
        ), PersistentDataType.BOOLEAN)

        val rightIsEnchantable = right?.persistentDataContainer?.get(NamespacedKey(
            ProgressionPlus.getPlugin(),
            "enchantable"
        ), PersistentDataType.BOOLEAN)

        if (leftIsEnchantable == false || rightIsEnchantable == false) {
            e.result = null
        }
    }

    @EventHandler
    fun onEnchantAttempt(e: PrepareItemEnchantEvent) {
        val item = e.item
        val itemIsEnchantable = item.persistentDataContainer.get(NamespacedKey(
            ProgressionPlus.getPlugin(),
            "enchantable"
        ), PersistentDataType.BOOLEAN)

        if (itemIsEnchantable != true) {
            e.isCancelled = true
        }
    }

}
