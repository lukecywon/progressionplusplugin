package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.items.SacrificialClock
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

object SacrificialClockGUI {
    private val activeInventories = mutableMapOf<Player, Inventory>()

    fun open(player: Player) {
        val inv = Bukkit.createInventory(null, 27, Component.text("§6Sacrificial Clock"))
        activeInventories[player] = inv
        player.openInventory(updateDisplay(inv))
    }

    fun handleClick(e: InventoryClickEvent) {
        if (e.slot == 26) {
            val player = e.whoClicked as? Player ?: return
            val inv = activeInventories[player] ?: return
            val score = inv.contents.filterNotNull().sumOf {
                it.amount * SacrificialClock.getScoreFromItem(it)
            }
            player.closeInventory()
            activeInventories.remove(player)
            object : BukkitRunnable() {
                override fun run() {
                    SacrificialClock.applyTierBuffs(player, score)
                }
            }.runTask(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin())
            return
        }
        val player = e.whoClicked as? Player ?: return
        val inv = activeInventories[player] ?: return
        if (e.clickedInventory != inv) return

        e.isCancelled = true
        Bukkit.getScheduler().runTaskLater(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), Runnable {
            updateDisplay(inv)
        }, 1L)
    }

    private fun updateDisplay(inv: Inventory): Inventory {
        val score = inv.contents.filterNotNull().sumOf {
            it.amount * SacrificialClock.getScoreFromItem(it)
        }

        val tier = when {
            score >= 5000 -> "§6Tier V"
            score >= 2500 -> "§5Tier IV"
            score >= 1000 -> "§9Tier III"
            score >= 300  -> "§aTier II"
            score >= 100  -> "§fTier I"
            else -> "§7None"
        }

        val displayItem = ItemStack(Material.GOLD_INGOT)
        val meta = displayItem.itemMeta
        meta.displayName(Component.text("§eSacrifice Total: §f$score"))
        meta.lore(listOf(Component.text("§7Current Tier: $tier"), Component.text("§8Offer golden items to gain blessings.")))
        displayItem.itemMeta = meta

        inv.setItem(13, displayItem)
        val confirmItem = ItemStack(Material.LIME_CONCRETE)
        val confirmMeta = confirmItem.itemMeta
        confirmMeta.displayName(Component.text("§aClick to Confirm Sacrifice"))
        confirmMeta.lore(listOf(Component.text("§7Right-click to receive blessings.")))
        confirmItem.itemMeta = confirmMeta

        inv.setItem(26, confirmItem)
        return inv
    }

    fun isSacrificeInventory(inventory: Inventory?): Boolean {
        return activeInventories.containsValue(inventory)
    }
}
