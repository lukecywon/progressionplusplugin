package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.utility.rare.RecallPotion
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitTask
import java.util.*

class RecallPotionListener : Listener {
    private val activeRecalls = mutableMapOf<UUID, BukkitTask>()
    private val recallCooldowns = mutableMapOf<UUID, Long>()  // Stores last used timestamps

    // Use your existing cooldown system
    val itemId = "recall_potion"
    val cooldownMillis = 5 * 60 * 1000L


    @EventHandler
    fun onDrinkRecallPotion(e: PlayerItemConsumeEvent) {
        val player = e.player
        val uuid = player.uniqueId

        if (!RecallPotion.isRecallPotion(e.item)) return

        // Cooldown check
        if (CustomItem.isOnCooldown(itemId, uuid)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, uuid)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cRecall Potion is on cooldown for §e${minutes}m ${seconds}s§c!")
            return
        }

        player.sendMessage("§bRecalling in 5 seconds. Don’t move or take damage!")
        player.playSound(player.location, Sound.BLOCK_PORTAL_AMBIENT, 0.6f, 1.4f)

        val task = Bukkit.getScheduler().runTaskTimer(ProgressionPlus.getPlugin(), object : Runnable {
            var secondsLeft = 5
            override fun run() {
                if (!player.isOnline || player.isDead) {
                    cancelRecallWithRefund(player)
                    return
                }

                if (secondsLeft <= 0) {
                    val overworld = Bukkit.getWorlds().firstOrNull { it.environment == org.bukkit.World.Environment.NORMAL }
                    val loc = player.bedSpawnLocation ?: overworld?.spawnLocation ?: player.world.spawnLocation
                    if (player.world.environment != org.bukkit.World.Environment.NORMAL && player.bedSpawnLocation == null) {
                        player.sendMessage("§eNo bed set — teleporting you to the Overworld spawn.")
                    }
                    player.teleportAsync(loc)
                    player.sendMessage("§aRecalled!")
                    player.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f)


                    CustomItem.setCooldown(itemId, uuid, cooldownMillis)

                    activeRecalls.remove(uuid)?.cancel()
                } else {
                    player.sendActionBar("§eTeleporting in §c$secondsLeft...")
                    secondsLeft--
                }
            }
        }, 0L, 20L)

        activeRecalls[uuid] = task
    }

    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        val player = e.entity
        if (player is Player && activeRecalls.containsKey(player.uniqueId)) {
            player.sendMessage("§cRecall interrupted!")
            player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.7f)
            cancelRecallWithRefund(player)
        }
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val player = e.player
        val uuid = player.uniqueId

        if (!activeRecalls.containsKey(uuid)) return

        val from = e.from
        val to = e.to ?: return

        if (from.blockX != to.blockX || from.blockY != to.blockY || from.blockZ != to.blockZ) {
            player.sendMessage("§cRecall canceled due to movement!")
            player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.7f)
            cancelRecallWithRefund(player)
            e.isCancelled = true
        }
    }

    private fun cancelRecallWithRefund(player: Player) {
        val uuid = player.uniqueId
        activeRecalls.remove(uuid)?.cancel()

        val potion = RecallPotion.createItemStack()
        val inventory = player.inventory
        val leftover = inventory.addItem(potion)

        if (leftover.isNotEmpty()) {
            leftover.values.forEach { player.world.dropItemNaturally(player.location, it) }
        }

        val glassBottle = inventory.contents.firstOrNull { it != null && it.type == Material.GLASS_BOTTLE }
        if (glassBottle != null) {
            glassBottle.amount -= 1
            if (glassBottle.amount <= 0) {
                inventory.remove(glassBottle)
            }
        }

        player.sendMessage("§eYour Recall Potion was returned!")
    }
}
