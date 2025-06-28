package com.lukecywon.progressionPlus.listeners.utility.rare

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.uncommon.BerserkerSword
import com.lukecywon.progressionPlus.items.utility.uncommon.MaxHeartFruit
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot

class MaxHeartFruitListener : Listener {
    @EventHandler
    fun onHeartFruitConsume(e: PlayerItemConsumeEvent) {
        val player = e.player
        val item = e.item

        if (!MaxHeartFruit.isThisItem(item)) return

        val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val maxHealth = attr.baseValue

        // ❌ Already max: just cancel
        if (maxHealth >= 20.0) {
            e.isCancelled = true
            player.sendMessage("§cYou already have the maximum number of hearts!")
            return
        }

        // ✅ Otherwise: apply heart, cancel event, manually remove 1 item
        e.isCancelled = true
        attr.baseValue = (maxHealth + 2.0).coerceAtMost(20.0)
        player.sendMessage("§dYou feel your heart grow stronger!")

        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            val slotItem = when (e.hand) {
                EquipmentSlot.HAND -> player.inventory.itemInMainHand
                EquipmentSlot.OFF_HAND -> player.inventory.itemInOffHand
                else -> return@Runnable
            }

            if (slotItem.isSimilar(item)) {
                if (slotItem.amount > 1) {
                    slotItem.amount -= 1
                } else {
                    when (e.hand) {
                        EquipmentSlot.HAND -> player.inventory.setItemInMainHand(null)
                        EquipmentSlot.OFF_HAND -> player.inventory.setItemInOffHand(null)
                        else -> {}
                    }
                }
            }

            if (e.hand == EquipmentSlot.OFF_HAND && BerserkerSword.isThisItem(player.inventory.itemInMainHand)) {
                player.showTitle(
                    Title.title(
                        Component.text("Surging Power!").color(NamedTextColor.GOLD),
                        Component.text("It will fade when you let go of the Berserker Sword.").color(NamedTextColor.GRAY)
                    )
                )
                player.playSound(player.location, Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f)
            }
        }, 1L)


        player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
    }
}
