package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.TwilightCrown
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class TwilightCrownListener : Listener {
    @EventHandler
    fun onHelmetEquip(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return

        // Check if the click is targeting the helmet slot in the player's own inventory
        if (e.slotType != InventoryType.SlotType.ARMOR || e.slot != 39) return

        val plugin = ProgressionPlus.getPlugin()
        val previousItem = e.currentItem?.clone()

        object : BukkitRunnable() {
            override fun run() {
                val newHelmet = player.inventory.helmet
                val hasCrownNow = TwilightCrown.isTwilightCrown(newHelmet)
                val hadCrownBefore = TwilightCrown.isTwilightCrown(previousItem)

                if (hasCrownNow && !hadCrownBefore) {
                    player.sendTitle("§6The Crown Takes Hold...", "§7You feel a dark presence...", 10, 60, 20)
                    player.playSound(player.location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 0.5f)
                } else if (!hasCrownNow && hadCrownBefore) {
                    player.sendTitle("§fClarity Returns", "§7The whispers fade away...", 10, 60, 20)
                    player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1.2f)
                }
            }
        }.runTaskLater(plugin, 1L)
    }

    @EventHandler
    fun onRightClickEquip(e: PlayerInteractEvent) {
        if (!e.hasItem()) return
        val item = e.item ?: return
        val player = e.player

        if (TwilightCrown.isTwilightCrown(item)) {
            // Check if they're holding the crown and trying to equip it
            if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                object : BukkitRunnable() {
                    override fun run() {
                        val newHelmet = player.inventory.helmet
                        if (TwilightCrown.isTwilightCrown(newHelmet)) {
                            player.sendTitle("§6The Crown Takes Hold...", "§7You feel a dark presence...", 10, 60, 20)
                            player.playSound(player.location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 0.5f)
                        }
                    }
                }.runTaskLater(ProgressionPlus.getPlugin(), 1L)
            }
        }
    }
}

