package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.VoidReaper
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent

class VoidReaperListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (VoidReaper.isThisItem(item)) {
            VoidReaper.unleashSouls(player, item)
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onLeftClick(e: PlayerInteractEvent) {
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (VoidReaper.isThisItem(item)) {
            VoidReaper.slashTeleport(player)
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        val item = killer.inventory.itemInMainHand

        if (VoidReaper.isThisItem(item)) {
            VoidReaper.addSoul(item)
            killer.sendMessage("§8☠ §7You collected a soul!")
            killer.world.playSound(killer.location, Sound.ENTITY_VEX_DEATH, 0.8f, 1.5f)
        }
    }

    @EventHandler
    fun onItemHeld(e: PlayerItemHeldEvent) {
        val player = e.player
        val item = player.inventory.getItem(e.newSlot) ?: return

        if (VoidReaper.isThisItem(item)) {
            player.sendActionBar("§5Souls stored: §d${VoidReaper.getSoulCount(item)}")
        }
    }
}
