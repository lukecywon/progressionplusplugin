package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.EchoBlade
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.HashMap

class EchoBladeListener : Listener {
    private data class HitTracker(var hits: Int, var lastHitTime: Long)

    private val hitCounts = mutableMapOf<UUID, HitTracker>()
    private val comboTasks = mutableMapOf<UUID, BukkitRunnable>()
    private val COMBO_TIMEOUT_MS = 3000L  // 6 seconds

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val attacker = e.damager as? Player ?: return
        val target = e.entity as? LivingEntity ?: return
        val item = attacker.inventory.itemInMainHand
        if (!EchoBlade.isEchoBlade(item)) return

        val uuid = attacker.uniqueId
        val now = System.currentTimeMillis()
        val tracker = hitCounts.getOrPut(uuid) { HitTracker(0, now) }

        // Reset if too slow
        if (now - tracker.lastHitTime > COMBO_TIMEOUT_MS) {
            tracker.hits = 0
        }

        tracker.hits++
        tracker.lastHitTime = now

        if (tracker.hits == 1) {
            startTimer(attacker)
        }

        if (tracker.hits >= 5) {
            e.damage += 3.0
            attacker.sendActionBar("§b✦ Echo Strike! Bonus damage dealt!")
            attacker.world.playSound(attacker.location, Sound.BLOCK_ANVIL_PLACE, 1f, 1.2f)
            attacker.world.spawnParticle(
                Particle.CRIT,
                target.location.add(0.0, 1.0, 0.0),
                15,
                0.3, 0.3, 0.3,
                0.05
            )
            stopTimer(uuid)
            hitCounts.remove(uuid)
        }
    }

    private fun startTimer(player: Player) {
        val uuid = player.uniqueId
        stopTimer(uuid) // In case there’s an old one

        val task = object : BukkitRunnable() {
            override fun run() {
                val tracker = hitCounts[uuid]
                if (tracker == null) {
                    cancel()
                    comboTasks.remove(uuid)
                    return
                }

                val now = System.currentTimeMillis()
                val timeLeft = ((COMBO_TIMEOUT_MS - (now - tracker.lastHitTime)) / 1000).coerceAtLeast(0)

                if (timeLeft <= 0) {
                    player.sendActionBar("§7Echo Combo expired.")
                    hitCounts.remove(uuid)
                    comboTasks.remove(uuid)
                    cancel()
                    return
                }

                player.sendActionBar("§7Echo Combo: §b${tracker.hits}§7/5  (§eTime left: ${timeLeft}s§7)")
            }
        }

        task.runTaskTimer(/* plugin = */ com.lukecywon.progressionPlus.ProgressionPlus.instance, 0L, 5L)
        comboTasks[uuid] = task
    }

    private fun stopTimer(uuid: UUID) {
        comboTasks.remove(uuid)?.cancel()
    }
}
