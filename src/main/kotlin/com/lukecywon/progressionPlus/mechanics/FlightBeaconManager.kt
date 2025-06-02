package com.lukecywon.progressionPlus.mechanics

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import java.util.*

object FlightBeaconManager {
    private val activeBeacons = mutableSetOf<Location>()
    private val fuelMap = mutableMapOf<Location, Long>() // Stores fuel end timestamps
    private val bossBars = mutableMapOf<UUID, BossBar>()

    private const val MAX_FUEL_MILLIS = 5 * 60 * 60 * 1000L // 5 hours
    private const val ONE_HOUR = 60 * 60 * 1000L

    fun registerBeacon(loc: Location) {
        activeBeacons.add(loc)
        fuelMap[loc] = System.currentTimeMillis() // Default to 0 time
    }

    fun unregisterBeacon(loc: Location) {
        activeBeacons.remove(loc)
        fuelMap.remove(loc)
    }

    fun isRegistered(location: Location): Boolean {
        return activeBeacons.contains(location)
    }

    fun addFuel(loc: Location): Boolean {
        val current = fuelMap[loc] ?: return false
        val now = System.currentTimeMillis()
        val currentFuel = maxOf(0, current - now)
        if (currentFuel >= MAX_FUEL_MILLIS) return false

        fuelMap[loc] = now + minOf(currentFuel + ONE_HOUR, MAX_FUEL_MILLIS)
        return true
    }

    fun getFuelRemainingMillis(loc: Location): Long {
        val endTime = fuelMap[loc] ?: return 0
        return maxOf(0, endTime - System.currentTimeMillis())
    }

    fun startFlightChecker(plugin: JavaPlugin) {
        object : BukkitRunnable() {
            override fun run() {
                for (world in Bukkit.getWorlds()) {
                    for (player in world.players) {
                        var inRange = false
                        var nearEdge = false
                        var closestFuelMillis = 0L

                        for (beaconLoc in activeBeacons) {
                            val beacon = beaconLoc.block
                            val tier = getBeaconTier(beacon)
                            val range = tier * 10

                            val dx = player.location.x - beaconLoc.x
                            val dz = player.location.z - beaconLoc.z
                            val horizontalDistance = Math.sqrt(dx * dx + dz * dz)

                            if (tier > 0 && horizontalDistance <= range) {
                                val fuelLeft = getFuelRemainingMillis(beaconLoc)
                                if (fuelLeft > 0) {
                                    inRange = true
                                    closestFuelMillis = fuelLeft
                                    if (horizontalDistance >= range * 0.8) {
                                        nearEdge = true
                                    }
                                    break
                                }
                            }
                        }

                        if (player.gameMode != GameMode.CREATIVE) {
                            player.allowFlight = inRange
                            if (!inRange && player.isFlying) {
                                player.isFlying = false
                            }
                        }

                        if (inRange) {
                            showBossBar(player, closestFuelMillis)
                            player.sendActionBar(if (nearEdge)
                                "§e⚠ Near edge of Flight Beacon range ⚠"
                            else
                                "§bYou are within a Flight Beacon zone")
                        } else {
                            hideBossBar(player)
                            if (player.allowFlight && player.gameMode != GameMode.CREATIVE) {
                                player.sendActionBar("§7You left the Flight Beacon range")
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L)
    }

    private fun showBossBar(player: Player, millis: Long) {
        val percent = millis.toDouble() / MAX_FUEL_MILLIS

        val bar = bossBars.computeIfAbsent(player.uniqueId) {
            val key = NamespacedKey(JavaPlugin.getPlugin(ProgressionPlus::class.java), "flight_fuel_${player.uniqueId}")
            Bukkit.getBossBar(key) ?: Bukkit.createBossBar(
                key,
                "Flight Fuel",
                BarColor.BLUE,
                BarStyle.SEGMENTED_20
            )
        }
        bar.progress = percent.coerceIn(0.0, 1.0)
        bar.setTitle("Flight Fuel: ${formatMillis(millis)}")
        bar.addPlayer(player)
    }

    private fun hideBossBar(player: Player) {
        bossBars[player.uniqueId]?.removePlayer(player)
    }

    fun getBeaconTier(beacon: Block): Int {
        val baseY = beacon.y - 1
        val world = beacon.world
        var tier = 0
        for (level in 1..4) {
            val y = baseY - (level - 1)
            for (x in -level..level) {
                for (z in -level..level) {
                    val type = world.getBlockAt(beacon.x + x, y, beacon.z + z).type
                    if (type != Material.DIAMOND_BLOCK) return tier
                }
            }
            tier = level
        }
        return tier
    }

    private fun formatMillis(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        val parts = mutableListOf<String>()
        if (hours > 0) parts.add("${hours}h")
        if (minutes > 0 || hours > 0) parts.add("${minutes}m")
        parts.add("${seconds}s")

        return parts.joinToString(" ")
    }


}