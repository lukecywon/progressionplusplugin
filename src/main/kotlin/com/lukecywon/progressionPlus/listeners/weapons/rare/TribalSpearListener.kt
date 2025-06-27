package com.lukecywon.progressionPlus.listeners.weapons.rare

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.rare.Peacemaker
import com.lukecywon.progressionPlus.items.weapons.rare.TribalSpear
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Trident
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class TribalSpearListener : Listener {
    private val itemId = "tribal_spear"
    private val cooldownMillis = 3_000L

    @EventHandler
    fun onThrow(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        // Only continue if it's a RIGHT click
        if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) return
        if (!TribalSpear.isThisItem(item)) return

        event.isCancelled = true // Only cancel when all checks are passed

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cTribal Spear is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)

        val world = player.world
        val direction = player.location.direction.normalize()
        var location = player.eyeLocation.clone()
        val speed = 0.5
        val maxTicks = 40 // 2 seconds
        var ticks = 0

        object : BukkitRunnable() {
            override fun run() {
                if (ticks++ > maxTicks) {
                    cancel()
                    return
                }

                location.add(direction.clone().multiply(speed))

                // If the projectile hits a block, stop it
                if (location.block.type.isSolid) {
                    cancel()
                    return
                }

                // Show particles
                world.spawnParticle(Particle.CRIT, location, 5, 0.1, 0.1, 0.1, 0.01)

                // Optional: damage nearby entities
                for (entity in world.getNearbyEntities(location, 1.0, 1.0, 1.0)) {
                    if (entity is LivingEntity && entity != player) {
                        entity.damage(5.0, player)
                        world.spawnParticle(Particle.DAMAGE_INDICATOR, entity.location, 10)
                        cancel()
                        return
                    }
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 1L) // Replace with your plugin instance
    }
}