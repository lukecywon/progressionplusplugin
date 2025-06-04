package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.VoidReaper
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class VoidReaperListener : Listener {

    private val slashCooldowns = mutableMapOf<UUID, Long>()
    private val notifiedReady = mutableSetOf<UUID>()
    private val burstCooldowns = mutableMapOf<UUID, Long>()
    private val SLASH_COOLDOWN_MS = 7_000L
    private val BURST_COOLDOWN_MS = 2000L

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!VoidReaper.isThisItem(item)) return

        val uuid = player.uniqueId
        val now = System.currentTimeMillis()
        val lastBurst = burstCooldowns.getOrDefault(uuid, 0L)

        if (now - lastBurst < BURST_COOLDOWN_MS) {
            val remaining = ((BURST_COOLDOWN_MS - (now - lastBurst)) / 1000.0).toInt()
            player.sendActionBar("§cVoid Reaper is recharging... §7(${remaining}s)")
            return
        }

        burstCooldowns[uuid] = now
        VoidReaper.unleashSouls(player, item)
        e.isCancelled = true
    }

    @EventHandler
    fun onLeftClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.LEFT_CLICK_AIR && e.action != Action.LEFT_CLICK_BLOCK) return

        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!VoidReaper.isThisItem(item)) return

        val uuid = player.uniqueId
        val now = System.currentTimeMillis()
        val last = slashCooldowns.getOrDefault(uuid, 0L)
        val elapsed = now - last

        if (elapsed < SLASH_COOLDOWN_MS) {
            val remaining = ((SLASH_COOLDOWN_MS - elapsed) / 1000.0).toInt()
            player.sendActionBar("§cSlash cooldown: §e${remaining}s")

            if (!notifiedReady.contains(uuid) && elapsed >= SLASH_COOLDOWN_MS - 1000) {
                player.sendActionBar("§a✔ Slash is ready!")
                notifiedReady.add(uuid)
            }
            return
        }

        val target = player.getTargetEntity(25) as? LivingEntity ?: run {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f)
            player.spawnParticle(Particle.SMOKE, player.eyeLocation, 10, 0.2, 0.2, 0.2, 0.01)

            // Add short cooldown (2s) for missing the slash
            slashCooldowns[uuid] = now - (SLASH_COOLDOWN_MS - 2000L)  // Ensures only 2s penalty
            player.sendActionBar("§7Missed. Cooldown: §e2s")
            Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
                notifiedReady.remove(uuid)
            }, 40L) // clear notified flag after 2s (40 ticks)

            return
        }

        val soulCount = VoidReaper.getSoulCount(item)
        val damage = 4.0 + soulCount * 0.3

        val behind = target.location.clone().add(target.location.direction.multiply(-1)).apply {
            y = target.location.y
        }

        player.teleport(behind)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.7f)
        player.playSound(player.location, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1f)

        target.damage(damage, player)
        target.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 1))
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 60, 1))

        player.spawnParticle(Particle.PORTAL, player.eyeLocation, 30, 0.5, 0.5, 0.5, 0.1)
        player.spawnParticle(Particle.SWEEP_ATTACK, target.location.add(0.0, 1.0, 0.0), 5)

        player.sendActionBar("§a☠ Slash executed!")

        slashCooldowns[uuid] = now
        notifiedReady.remove(uuid)

        e.isCancelled = true
    }

    @EventHandler
    fun onKill(e: EntityDeathEvent) {
        val killer = e.entity.killer ?: return
        val item = killer.inventory.itemInMainHand

        if (!VoidReaper.isThisItem(item)) return

        val soulsBefore = VoidReaper.getSoulCount(item)
        VoidReaper.addSoul(item)
        val soulsAfter = VoidReaper.getSoulCount(item)

        if (soulsBefore == soulsAfter) {
            killer.sendActionBar("§cMaximum souls reached!")
        } else {
            killer.sendActionBar("§a+1 Soul absorbed!")
        }

        killer.world.playSound(killer.location, Sound.ENTITY_VEX_DEATH, 0.8f, 1.5f)
    }

    @EventHandler
    fun onItemHeld(e: PlayerItemHeldEvent) {
        val player = e.player
        val item = player.inventory.getItem(e.newSlot) ?: return

        if (VoidReaper.isThisItem(item)) {
            player.sendActionBar("§5Souls stored: §d${VoidReaper.getSoulCount(item)}")
        }
    }
}
