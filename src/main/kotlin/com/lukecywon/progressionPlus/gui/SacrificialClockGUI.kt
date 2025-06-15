package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.items.utility.legendary.SacrificialClock
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
        val display = createDisplayItem(0)
        val confirm = createConfirmItem()

        inv.setItem(0, display) // Display score/tier
        inv.setItem(26, confirm) // Confirm button

        activeInventories[player] = inv
        player.openInventory(inv)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val inv = activeInventories[player] ?: return
        if (e.clickedInventory != inv) return

        e.isCancelled = true

        when (e.slot) {
            4 -> { // Confirm slot
                val score = inv.contents.slice(9..17).filterNotNull()
                    .filterNotNull()
                    .sumOf { it.amount * SacrificialClock.getScoreFromItem(it) }

                player.closeInventory()
                activeInventories.remove(player)

                object : BukkitRunnable() {
                    override fun run() {
                        SacrificialClock.applyTierBuffs(player, score)
                    }
                }.runTask(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin())
            }
        }

        Bukkit.getScheduler().runTaskLater(com.lukecywon.progressionPlus.ProgressionPlus.getPlugin(), Runnable {
            updateDisplay(inv)
        }, 1L)
    }

    private fun updateDisplay(inv: Inventory) {
        val score = inv.contents.slice(9..17).filterNotNull()
            .filterNotNull()
            .sumOf { it.amount * SacrificialClock.getScoreFromItem(it) }

        val display = createDisplayItem(score)
        inv.setItem(0, display)
    }

    private fun createDisplayItem(score: Int): ItemStack {
        val tier = when {
            score >= 5000 -> "§6Tier V"
            score >= 2500 -> "§5Tier IV"
            score >= 1000 -> "§9Tier III"
            score >= 300 -> "§aTier II"
            score >= 100 -> "§fTier I"
            else -> "§7None"
        }

        val item = ItemStack(Material.GOLD_INGOT)
        val meta = item.itemMeta
        meta.displayName(Component.text("§eSacrifice Total: §f$score"))
        meta.lore(listOf(Component.text("§7Current Tier: $tier"), Component.text("§8Insert golden items in slots 10–17.")))
        item.itemMeta = meta
        return item
    }

    private fun createConfirmItem(): ItemStack {
        val item = ItemStack(Material.LIME_CONCRETE)
        val meta = item.itemMeta
        meta.displayName(Component.text("§aClick to Confirm Sacrifice"))
        meta.lore(listOf(Component.text("§7Confirm to receive blessings.")))
        item.itemMeta = meta
        return item
    }

    fun isSacrificeInventory(inventory: Inventory?): Boolean {
        return activeInventories.containsValue(inventory)
    }
}
