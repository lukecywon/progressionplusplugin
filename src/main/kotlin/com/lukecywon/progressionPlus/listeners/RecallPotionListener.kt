package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.RecallPotion
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

    @EventHandler
    fun onDrinkRecallPotion(e: PlayerItemConsumeEvent) {
        val player = e.player

        if (!RecallPotion.isRecallPotion(e.item)) return

        val uuid = player.uniqueId

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
                    val loc = player.bedSpawnLocation ?: player.world.spawnLocation
                    player.teleportAsync(loc)
                    player.sendMessage("§aRecalled!")
                    player.playSound(loc, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1f, 1f)
                    activeRecalls.remove(uuid)?.cancel()
                } else {
                    player.sendActionBar("§eTeleporting in §c$secondsLeft...")
                    secondsLeft--
                }
            }
        }, 0L, 20L) // 20L = 1 second

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

        // Block-level movement check
        if (from.x.toInt() != to.x.toInt() ||
            from.y.toInt() != to.y.toInt() ||
            from.z.toInt() != to.z.toInt()) {

            player.sendMessage("§cRecall canceled due to movement!")
            player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.7f)
            cancelRecallWithRefund(player)
            e.isCancelled = true
        }
    }

    private fun cancelRecallWithRefund(player: Player) {
        val uuid = player.uniqueId
        activeRecalls.remove(uuid)?.cancel()

        // Refund the recall potion
        val potion = RecallPotion.createItemStack()
        val inventory = player.inventory

        // Add potion back or drop
        val leftover = inventory.addItem(potion)
        if (leftover.isNotEmpty()) {
            leftover.values.forEach { player.world.dropItemNaturally(player.location, it) }
        }

        // 🔥 Remove 1 glass bottle
        val glassBottle = inventory.contents.firstOrNull { it != null && it.type == Material.GLASS_BOTTLE }
        if (glassBottle != null) {
            glassBottle.amount -= 1
            if (glassBottle.amount <= 0) {
                inventory.remove(glassBottle)
            }
        }

        player.sendMessage("§eYour Recall Potion was returned, but the bottle shattered.")
    }

}
