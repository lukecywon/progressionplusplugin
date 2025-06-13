package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.PhoenixTotem
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.scheduler.BukkitRunnable
import net.kyori.adventure.title.Title
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import java.time.Duration
import java.util.*

class PhoenixTotemListener : Listener {

    private val phoenixTotemUsers = mutableSetOf<UUID>()

    @EventHandler
    fun onPhoenixTotemUse(e: EntityResurrectEvent) {
        val player = e.entity as? Player ?: return

        if (!phoenixTotemUsers.remove(player.uniqueId)) return // Used normal totem or wasn't marked

        val deathLocation = player.location.clone()

        object : BukkitRunnable() {
            override fun run() {
                val overworld = Bukkit.getWorlds().firstOrNull { it.environment == World.Environment.NORMAL }
                val rawSpawn = player.bedSpawnLocation ?: overworld?.spawnLocation ?: player.world.spawnLocation
                val safeSpawn = findSafeSpawn(rawSpawn)
                player.teleport(safeSpawn)

                spawnMassiveFireBurst(deathLocation, 2.5, 300)
                spawnFireRings(deathLocation.clone().add(0.0, 0.5, 0.0))

                object : BukkitRunnable() {
                    override fun run() {
                        val spawnLocation = player.location.clone()

                        spawnMassiveFireBurst(spawnLocation, 2.5, 300)
                        spawnFireRings(spawnLocation.clone().add(0.0, 0.5, 0.0))

                        player.showTitle(
                            Title.title(
                                Component.text("§cREBORN IN FLAMES"),
                                Component.text("§6You were saved by the Phoenix Totem"),
                                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
                            )
                        )
                        player.playSound(spawnLocation, Sound.ITEM_TOTEM_USE, 1f, 1f)
                        player.sendMessage("§6The Phoenix Totem erupts in flames and brings you back!")
                    }
                }.runTaskLater(ProgressionPlus.instance, 2L)
            }
        }.runTaskLater(ProgressionPlus.instance, 1L)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPreDeath(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        if (e.finalDamage < player.health) return // Not fatal

        val main = player.inventory.itemInMainHand
        val off = player.inventory.itemInOffHand

        // Minecraft always prioritizes main hand totem
        val willUse = if (main.type == Material.TOTEM_OF_UNDYING) main else if (off.type == Material.TOTEM_OF_UNDYING) off else return

        if (PhoenixTotem.isPhoenixTotem(willUse)) {
            phoenixTotemUsers.add(player.uniqueId)
        }
    }



    private fun spawnMassiveFireBurst(center: Location, radius: Double = 2.5, density: Int = 200) {
        val world = center.world

        for (i in 0 until density) {
            val x = (Math.random() - 0.5) * 2 * radius
            val y = (Math.random() - 0.3) * 2 * radius // slightly more height
            val z = (Math.random() - 0.5) * 2 * radius
            val pos = center.clone().add(x, y, z)

            world.spawnParticle(Particle.FLAME, pos, 0, 0.0, 0.05, 0.0)
            if (Math.random() < 0.3) world.spawnParticle(Particle.LAVA, pos, 0)
            if (Math.random() < 0.2) world.spawnParticle(Particle.LARGE_SMOKE, pos, 0)
            if (Math.random() < 0.1) world.spawnParticle(Particle.EXPLOSION, pos, 0)
        }
    }

    private fun spawnFireRings(center: Location, layers: Int = 6, pointsPerRing: Int = 60, ringSpacing: Double = 0.5, maxRadius: Double = 3.0) {
        val world = center.world

        for (i in 0 until layers) {
            val radius = maxRadius * ((layers - i).toDouble() / layers.toDouble()) // smaller on top
            val yOffset = i * ringSpacing

            for (j in 0 until pointsPerRing) {
                val angle = 2 * Math.PI * j / pointsPerRing
                val x = radius * Math.cos(angle)
                val z = radius * Math.sin(angle)
                val particleLoc = center.clone().add(x, yOffset, z)

                world.spawnParticle(Particle.FLAME, particleLoc, 0, 0.0, 0.0, 0.0, 0.01)
                if (Math.random() < 0.2) {
                    world.spawnParticle(Particle.LAVA, particleLoc, 0)
                }
            }
        }
    }

    private fun findSafeSpawn(start: Location): Location {
        val world = start.world
        var loc = start.clone()
        val maxY = world.maxHeight

        // Step 1: Move up if inside blocks
        while (loc.block.type != Material.AIR && loc.y < maxY) {
            loc.add(0.0, 1.0, 0.0)
        }

        // Step 2: Ensure 2 blocks of headroom
        val headroom = loc.clone().add(0.0, 1.0, 0.0)
        if (headroom.block.type != Material.AIR) {
            loc = world.getHighestBlockAt(loc).location.add(0.5, 1.0, 0.5)
        }

        // Step 3: Prevent void or lava
        val below = loc.clone().add(0.0, -1.0, 0.0)
        if (below.block.type == Material.AIR || below.block.isLiquid || below.block.type == Material.LAVA) {
            loc = world.getHighestBlockAt(loc).location.add(0.5, 1.0, 0.5)
        }

        return loc
    }


}
