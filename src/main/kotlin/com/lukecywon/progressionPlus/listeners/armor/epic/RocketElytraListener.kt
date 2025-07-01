package com.lukecywon.progressionPlus.listeners.armor.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.armor.epic.RocketElytra
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityToggleGlideEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class RocketElytraListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()
    private val rocketKey = RocketElytra.rocketKey
    private val maxCharges = 3
    private val regenIntervalTicks = 200L // 10 seconds
    private val playerCharges = mutableMapOf<UUID, Int>()

    init {
        // Passive charge regeneration
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (!RocketElytra.isThisItem(player.inventory.chestplate)) continue
                    val uuid = player.uniqueId
                    val current = playerCharges.getOrDefault(uuid, maxCharges)
                    if (current < maxCharges) {
                        playerCharges[uuid] = current + 1
                        player.sendActionBar(
                            Component.text("§aCharge Regenerated! (${playerCharges[uuid]}/$maxCharges)")
                        )
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, regenIntervalTicks)
    }

    @EventHandler
    fun onGlideStart(event: EntityToggleGlideEvent) {
        val player = event.entity as? Player ?: return
        if (!event.isGliding) return

        val chestItem = player.inventory.chestplate ?: return
        val meta = chestItem.itemMeta ?: return
        val data = meta.persistentDataContainer

        if (!data.has(rocketKey, PersistentDataType.BYTE)) return

        if (player.velocity.y > -0.2) {
            player.velocity = player.velocity.setY(0.0).add(Vector(0.0, 2.4, 0.0))
            player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 0.5f)

            object : BukkitRunnable() {
                var ticks = 0
                override fun run() {
                    if (ticks++ > 20 || !player.isGliding) {
                        cancel()
                        return
                    }
                    player.world.spawnParticle(
                        Particle.SOUL_FIRE_FLAME,
                        player.location.clone().subtract(0.0, 0.5, 0.0),
                        12, 0.2, 0.1, 0.2, 0.01
                    )
                }
            }.runTaskTimer(plugin, 0L, 1L)
        }
    }

    @EventHandler
    fun onSneakBoost(event: PlayerToggleSneakEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (!event.isSneaking) return
        if (!player.isGliding) return
        if (!RocketElytra.isThisItem(player.inventory.chestplate)) return

        val charges = playerCharges.getOrDefault(uuid, maxCharges)
        if (charges <= 0) {
            player.sendActionBar(Component.text("§cNo rocket charges!", NamedTextColor.RED))
            return
        }

        playerCharges[uuid] = charges - 1

        val direction = player.location.direction.normalize()
        val boost = direction.multiply(1.6).add(Vector(0.0, 0.15, 0.0))
        player.velocity = player.velocity.add(boost)

        player.world.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f)
        player.world.spawnParticle(Particle.FIREWORK, player.location, 20, 0.2, 0.2, 0.2, 0.05)
        player.sendActionBar(
            Component.text("§eRocket Boost! §7(${playerCharges[uuid]}/$maxCharges)")
        )

        // 🔥 Flame trail for 2 seconds (40 ticks)
        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (ticks++ > 40 || !player.isGliding) {
                    cancel()
                    return
                }
                player.world.spawnParticle(
                    Particle.SOUL_FIRE_FLAME,
                    player.location.clone().subtract(0.0, 0.5, 0.0),
                    12, 0.2, 0.1, 0.2, 0.01
                )
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

}
