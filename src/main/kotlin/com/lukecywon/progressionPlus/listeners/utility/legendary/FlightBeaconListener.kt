package com.lukecywon.progressionPlus.listeners.utility.legendary

import com.lukecywon.progressionPlus.items.utility.legendary.FlightBeacon
import com.lukecywon.progressionPlus.mechanics.FlightBeaconManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class FlightBeaconListener : Listener {

    @EventHandler
    fun onPlace(e: BlockPlaceEvent) {
        val block = e.block
        val item = e.itemInHand

        if (FlightBeacon.isThisItem(item)) {
            FlightBeaconManager.registerBeacon(block.location)
            e.player.sendMessage("§bFlight Beacon placed!")
        }
    }

    @EventHandler
    fun onBreak(e: BlockBreakEvent) {
        val loc = e.block.location
        if (FlightBeaconManager.isRegistered(loc)) {
            FlightBeaconManager.unregisterBeacon(loc)
            e.player.sendMessage("§cFlight Beacon removed.")
            e.isDropItems = false // Don't drop default beacon
            loc.world.dropItemNaturally(loc, FlightBeacon.createItemStack())
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return
        val player = e.player
        val loc = block.location

        if (block.type != Material.BEACON || !FlightBeaconManager.isRegistered(loc)) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true // Prevent vanilla beacon GUI

        val currentFuel = FlightBeaconManager.getFuelRemainingMillis(loc)
        val heldItem = player.inventory.itemInMainHand

        if (heldItem.type == Material.NETHERITE_INGOT) {
            val added = FlightBeaconManager.addFuel(loc)
            if (added) {
                heldItem.amount -= 1
                val total = FlightBeaconManager.getFuelRemainingMillis(loc)
                player.sendMessage("§aAdded 1 hour of flight time! Total: ${formatMillis(total)}")
            } else {
                player.sendMessage("§cFlight Beacon is fully fueled (5 hours max).")
            }
        } else {
            player.sendMessage("§bRemaining flight time: ${formatMillis(currentFuel)}")
        }
    }

    private fun formatMillis(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d:%02d", hours, minutes, seconds)
    }
}