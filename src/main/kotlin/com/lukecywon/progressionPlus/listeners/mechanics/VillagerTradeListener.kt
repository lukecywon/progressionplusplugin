package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.VillagerAcquireTradeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe

class VillagerTradeListener : Listener {
    private val plugin = ProgressionPlus.getPlugin()
    private val diamondItems = setOf(
        Material.DIAMOND_SWORD,
        Material.DIAMOND_AXE,
        Material.DIAMOND_PICKAXE,
        Material.DIAMOND_SHOVEL,
        Material.DIAMOND_HOE,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS
    )

    @EventHandler
    fun onVillagerSpawn(event: CreatureSpawnEvent) {
        if (plugin.config.getBoolean("diamond-unlocked")) return

        val villager = event.entity as? Villager ?: return

        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val newTrades = mutableListOf<MerchantRecipe>()

            for (recipe in villager.recipes) {
                if (recipe.result.type in diamondItems) {
                    val replacement = getIronReplacement(recipe.result.type)
                    if (replacement != null) {
                        val newRecipe = MerchantRecipe(replacement, 10)
                        newRecipe.addIngredient(ItemStack(Material.EMERALD, 5))
                        newTrades.add(newRecipe)
                    }
                } else {
                    newTrades.add(recipe)
                }
            }

            villager.setRecipes(newTrades)
        }, 1L)
    }

    @EventHandler
    fun onAcquireTrade(event: VillagerAcquireTradeEvent) {
        if (plugin.config.getBoolean("diamond-unlocked")) return

        val result = event.recipe.result.type

        if (result in diamondItems) {
            event.isCancelled = true

            // Optionally replace with iron equivalent:
            val replacement = getIronReplacement(result) ?: return
            val newRecipe = MerchantRecipe(replacement, 10)
            newRecipe.addIngredient(ItemStack(Material.EMERALD, 5))

            event.recipe = newRecipe
        }
    }

    private fun getIronReplacement(diamond: Material): ItemStack? {
        return when (diamond) {
            Material.DIAMOND_SWORD -> ItemStack(Material.IRON_SWORD)
            Material.DIAMOND_AXE -> ItemStack(Material.IRON_AXE)
            Material.DIAMOND_PICKAXE -> ItemStack(Material.IRON_PICKAXE)
            Material.DIAMOND_SHOVEL -> ItemStack(Material.IRON_SHOVEL)
            Material.DIAMOND_HOE -> ItemStack(Material.IRON_HOE)
            Material.DIAMOND_HELMET -> ItemStack(Material.IRON_HELMET)
            Material.DIAMOND_CHESTPLATE -> ItemStack(Material.IRON_CHESTPLATE)
            Material.DIAMOND_LEGGINGS -> ItemStack(Material.IRON_LEGGINGS)
            Material.DIAMOND_BOOTS -> ItemStack(Material.IRON_BOOTS)
            else -> null
        }
    }
}