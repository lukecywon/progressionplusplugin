package com.lukecywon.progressionPlus.listeners.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.legendary.FamesAuri
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class FamesAuriListener : Listener {

    private val activePlayers = ConcurrentHashMap<UUID, Int>()
    private val activeModes = ConcurrentHashMap<UUID, Int>()
    private val scheduledTasks = ConcurrentHashMap<UUID, BukkitRunnable>()
    private val particleTasks = ConcurrentHashMap<UUID, BukkitRunnable>()
    private val pendingActivations = ConcurrentHashMap<UUID, BukkitRunnable>()
    private val recentlyDeactivated = ConcurrentHashMap<UUID, Long>()

    @EventHandler
    fun onDrop(e: PlayerDropItemEvent) {
        val player = e.player
        if (FamesAuri.isFamesAuri(e.itemDrop.itemStack)) {
            deactivate(player)
        }
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        val player = e.player
        val item = player.inventory.itemInMainHand
        if (!FamesAuri.isFamesAuri(item)) return

        if (player.isSneaking) {
            cycleMode(player)
        } else {
            val lastDeactivated = recentlyDeactivated[player.uniqueId] ?: 0L
            if (System.currentTimeMillis() - lastDeactivated < 2000L) {
                player.sendMessage("§7You must wait before toggling again.")
                return
            }
            toggle(player)
        }
    }

    @EventHandler
    fun onSwapHotbarSlot(e: PlayerItemHeldEvent) {
        deactivate(e.player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        deactivate(e.player)
    }

    private fun toggle(player: Player) {
        val uuid = player.uniqueId

        if (scheduledTasks.containsKey(uuid) || pendingActivations.containsKey(uuid)) {
            deactivate(player)
            return
        }

        val selectedMode = activeModes.getOrElse(uuid) {
            determineBuffMode(player) ?: -1
        }

        val validMode = if (selectedMode != -1 && player.inventory.contains(FamesAuri.buffs[selectedMode].material)) {
            selectedMode
        } else {
            determineBuffMode(player) ?: run {
                player.sendMessage("§cNo gold treasure to consume.")
                return
            }
        }

        val config = FamesAuri.buffs[validMode]
        activePlayers[uuid] = validMode
        activeModes[uuid] = validMode
        player.sendMessage("§e${config.label} Fames Auri activating in ${config.activationDelayTicks / 20} seconds...")
        player.playSound(player.location, Sound.BLOCK_BEACON_POWER_SELECT, 1.0f, 1.2f)

        val pendingTask = object : BukkitRunnable() {
            override fun run() {
                pendingActivations.remove(uuid)

                if (!player.isOnline || !FamesAuri.isFamesAuri(player.inventory.itemInMainHand)) {
                    deactivate(player)
                    return
                }

                player.world.spawnParticle(Particle.END_ROD, player.location.add(0.0, 1.0, 0.0), 20, 0.3, 0.5, 0.3, 0.01)
                player.playSound(player.location, Sound.ITEM_TOTEM_USE, 1.0f, 1.1f)

                val task = object : BukkitRunnable() {
                    override fun run() {
                        if (!player.isOnline || !FamesAuri.isFamesAuri(player.inventory.itemInMainHand)) {
                            deactivate(player)
                            return
                        }

                        if (!player.inventory.contains(config.material)) {
                            player.sendMessage("§cOut of ${config.material}.")
                            deactivate(player)
                            return
                        }

                        player.inventory.removeItem(ItemStack(config.material, 1))
                        config.potionEffects.forEach { player.addPotionEffect(it) }
                        config.attributeModifiers.forEach { (attr, mod) ->
                            val attrObj = player.getAttribute(attr)
                            if (attrObj != null && attrObj.getModifier(mod.uniqueId) == null) {
                                attrObj.addModifier(mod)
                            }
                        }
                    }
                }

                task.runTaskTimer(ProgressionPlus.getPlugin(), 0L, config.intervalTicks)
                scheduledTasks[uuid] = task

                val particleTask = object : BukkitRunnable() {
                    override fun run() {
                        if (!player.isOnline || !FamesAuri.isFamesAuri(player.inventory.itemInMainHand)) {
                            cancel()
                            return
                        }
                        val (count, size) = when (validMode) {
                            0 -> 5 to 1.0f
                            1 -> 10 to 1.5f
                            else -> 20 to 2.0f
                        }
                        val brightGold = Color.fromRGB(255, 200, 50)
                        val softGold = Color.fromRGB(255, 190, 40)
                        val dust = Particle.DustOptions(brightGold, size)
                        val blendDust = Particle.DustOptions(softGold, size * 0.8f)
                        val loc = player.location.clone().add(0.0, 0.3, 0.0)

                        player.world.spawnParticle(Particle.DUST, loc, count, 0.3, 0.1, 0.3, 0.01, dust)
                        player.world.spawnParticle(Particle.DUST, loc, count / 2, 0.2, 0.1, 0.2, 0.01, blendDust)
                    }
                }

                particleTask.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 5L)
                particleTasks[uuid] = particleTask
            }
        }

        pendingTask.runTaskLater(ProgressionPlus.getPlugin(), config.activationDelayTicks)
        pendingActivations[uuid] = pendingTask
    }

    private fun deactivate(player: Player) {
        val uuid = player.uniqueId
        if (!activePlayers.containsKey(uuid) && !scheduledTasks.containsKey(uuid) && !pendingActivations.containsKey(uuid)) return

        activePlayers.remove(uuid)
        scheduledTasks.remove(uuid)?.cancel()
        pendingActivations.remove(uuid)?.cancel()
        particleTasks.remove(uuid)?.cancel()

        FamesAuri.buffs.flatMap { it.potionEffects }.forEach {
            player.removePotionEffect(it.type)
        }

        FamesAuri.buffs.flatMap { it.attributeModifiers }.forEach { (attr, mod) ->
            player.getAttribute(attr)?.removeModifier(mod)
        }

        player.playSound(player.location, Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 0.8f)
        player.sendMessage("§6Fames Auri has been deactivated.")
        recentlyDeactivated[player.uniqueId] = System.currentTimeMillis()
    }

    private fun cycleMode(player: Player) {
        val uuid = player.uniqueId
        val current = activeModes.getOrDefault(uuid, 0)
        val next = (current + 1) % FamesAuri.buffs.size
        activeModes[uuid] = next
        player.sendMessage("§eFames Auri set to ${FamesAuri.buffs[next].label}.")
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 0.7f, 1.3f)
        player.world.spawnParticle(Particle.CRIT, player.location.add(0.0, 1.0, 0.0), 15, 0.4, 0.5, 0.4, 0.01)
    }

    private fun determineBuffMode(player: Player): Int? {
        for ((index, config) in FamesAuri.buffs.withIndex()) {
            if (player.inventory.contains(config.material)) return index
        }
        return null
    }
}
