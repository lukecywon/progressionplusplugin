package com.lukecywon.progressionPlus.listeners.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.EnderDragon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

class DragonBuffListener : Listener {

    @EventHandler
    fun onDragonSpawn(event: CreatureSpawnEvent) {
        val dragon = event.entity as? EnderDragon ?: return

        // Spawn it far away first
        val world = dragon.world
        val spawnLoc = dragon.location.clone()
        val farLoc = spawnLoc.clone().add(0.0, 500.0, 0.0) // Spawn way up in the air

        dragon.teleport(farLoc)

        // Delay a few ticks and then teleport to actual location
        object : BukkitRunnable() {
            override fun run() {
                if (!dragon.isDead && dragon.isValid) {
                    dragon.teleport(spawnLoc) // Teleport back to desired fight location
                }
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 5L) // ~0.25 sec delay

        // Now continue your health and boss bar logic as usual
        val baseHealth = 500.0
        val bonusPerPlayer = 50.0
        val playerCount = Bukkit.getOnlinePlayers().size
        val totalHealth = baseHealth + (bonusPerPlayer * playerCount)

        val attr = dragon.getAttribute(Attribute.MAX_HEALTH)
        if (attr != null) {
            attr.baseValue = totalHealth
            dragon.health = totalHealth
        }

        val bossBar: BossBar = Bukkit.createBossBar(
            "Ender Dragon: ${totalHealth.toInt()} / ${totalHealth.toInt()} HP",
            BarColor.PURPLE,
            BarStyle.SEGMENTED_20
        )
        bossBar.isVisible = true

        object : BukkitRunnable() {
            override fun run() {
                if (!dragon.isValid || dragon.isDead) {
                    bossBar.removeAll()
                    bossBar.isVisible = false
                    cancel()
                    return
                }

                bossBar.players.forEach {
                    if (!it.isOnline || it.world != dragon.world) bossBar.removePlayer(it)
                }
                Bukkit.getOnlinePlayers()
                    .filter { it.world == dragon.world && !bossBar.players.contains(it) }
                    .forEach { bossBar.addPlayer(it) }

                val current = dragon.health.coerceAtLeast(0.0).toInt()
                val max = dragon.getAttribute(Attribute.MAX_HEALTH)?.baseValue?.toInt() ?: totalHealth.toInt()
                bossBar.setTitle("Ender Dragon: $current / $max HP")
                bossBar.progress = (current / max.toDouble()).coerceIn(0.0, 1.0)
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 5L)
    }

}
