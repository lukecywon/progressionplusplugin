package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.BerserkerSword
import com.lukecywon.progressionPlus.items.HealthCrystal
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

class HealthCrystalListener : Listener {
    @EventHandler
    fun onHealthCrystalConsume(e: PlayerItemConsumeEvent) {
        val player = e.player
        val item = e.item

        if (!HealthCrystal.isHealthCrystal(item)) return

        val attr = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val maxHealth = attr.baseValue

        // ❌ Only allow if player has more than 10 hearts (20.0 health)
        if (maxHealth < 20.0) {
            e.isCancelled = true
            player.sendMessage(Component.text("§cYou feel too week to absorb the Health Crystal."))
            return
        }

        // ❌ Already maxed at 40.0
        if (maxHealth >= 40.0) {
            e.isCancelled = true
            player.sendMessage(Component.text("Your body can't handle more health!").color(NamedTextColor.RED))
            return
        }

        // Apply effect
        e.isCancelled = true
        attr.baseValue = (maxHealth + 2.0).coerceAtMost(40.0)
        player.sendMessage(Component.text("The crystal fuses with your soul.").color(NamedTextColor.LIGHT_PURPLE))

        // Manually remove item
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

            if (e.hand == EquipmentSlot.OFF_HAND && BerserkerSword.isBerserkerSword(player.inventory.itemInMainHand)) {
                player.showTitle(
                    Title.title(
                        Component.text("Surging Power!").color(NamedTextColor.GOLD),
                        Component.text("It will fade when you let go of the Berserker Sword.").color(NamedTextColor.GRAY)
                    )
                )
                player.playSound(player.location, Sound.ENTITY_WITHER_SPAWN, 0.5f, 1.5f)
            }

        }, 1L)

        player.playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 1.2f)
    }
}
