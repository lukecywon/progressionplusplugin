package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.TwilightCrown
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.scheduler.BukkitRunnable

class TwilightCrownListener : Listener {
    @EventHandler
    fun onHelmetEquip(e: InventoryClickEvent) {
        if (e.slotType != InventoryType.SlotType.ARMOR || e.slot != 39) return  // 39 = helmet slot
        val player = e.whoClicked as? Player ?: return

        val plugin = ProgressionPlus.getPlugin()

        object : BukkitRunnable() {
            override fun run() {
                val helmet = player.inventory.helmet
                val hasCrownNow = TwilightCrown.isTwilightCrown(helmet)
                val hadCrownBefore = TwilightCrown.isTwilightCrown(e.currentItem)

                if (hasCrownNow && !hadCrownBefore) {
                    player.sendTitle("§6The Crown Takes Hold...", "§7You feel a dark presence...", 10, 60, 20)
                    player.playSound(player.location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 0.5f)
                } else if (!hasCrownNow && hadCrownBefore) {
                    player.sendTitle("§fClarity Returns", "§7The whispers fade away...", 10, 60, 20)
                    player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1.2f)
                }
            }
        }.runTaskLater(plugin, 1L) // Delay 1 tick to let the inventory update
    }
}

