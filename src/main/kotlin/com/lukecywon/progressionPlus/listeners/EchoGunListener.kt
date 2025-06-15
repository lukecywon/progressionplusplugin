package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.legendary.EchoGun
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class EchoGunListener : Listener {
    private val itemId = "echo_gun"
    private val cooldownMillis = 30_000L

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return
        val player = e.player

        if (EchoGun.isThisItem(player.inventory.itemInMainHand)) {
            if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
                val remaining = CustomItem.getCooldownRemaining(itemId, player.uniqueId) / 1000.0
                player.sendActionBar(
                    Component.text("⏳ Echo Gun on cooldown: %.1f seconds left".format(remaining))
                        .color(NamedTextColor.RED)
                )
                return
            }

            CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
            EchoGun.shootSonicBoom(player)
            e.isCancelled = true
        }
    }
}
