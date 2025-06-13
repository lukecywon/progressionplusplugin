package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.VerdantCleaver
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class VerdantCleaverListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()

    private val logTypes = setOf(
        Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG,
        Material.JUNGLE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG,
        Material.MANGROVE_LOG, Material.CHERRY_LOG, Material.BAMBOO_BLOCK,
        Material.CRIMSON_STEM, Material.WARPED_STEM
    )

    @EventHandler
    fun onTreeBreak(e: BlockBreakEvent) {
        val player = e.player
        val block = e.block
        val item = player.inventory.itemInMainHand

        if (!VerdantCleaver.isVerdantCleaver(item)) return
        if (block.type !in logTypes) return

        e.isCancelled = true

        val visited = mutableSetOf<Block>()

        object : BukkitRunnable() {
            override fun run() {
                breakConnectedLogsAnimated(player, block, visited, item)
            }
        }.runTaskLater(plugin, 1L)
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!VerdantCleaver.isVerdantCleaver(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true  // 💥 Prevent block interaction (e.g., making dirt paths)

        val newSize = VerdantCleaver.cycleVeinSize(item)
        player.sendActionBar(Component.text("Verdant Cleaver vein size set to $newSize"))
    }

    private fun breakConnectedLogsAnimated(player: Player, origin: Block, visited: MutableSet<Block>, tool: ItemStack?) {
        val toBreak = mutableListOf<Block>()
        val max = VerdantCleaver.getVeinSize(tool)
        collectLogs(origin, origin.type, visited, toBreak)

        val logs = toBreak.take(max)

        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i >= logs.size) {
                    cancel()
                    return
                }

                val log = logs[i]
                player.swingMainHand()
                log.breakNaturally(tool)
                player.playSound(log.location, Sound.BLOCK_WOOD_BREAK, 1f, 1.2f)

                i++
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    private fun collectLogs(
        start: Block,
        targetType: Material,
        visited: MutableSet<Block>,
        result: MutableList<Block>
    ) {
        val faceQueue = ArrayDeque<Block>()
        val diagonalQueue = ArrayDeque<Block>()

        faceQueue.add(start)
        visited.add(start)

        while ((faceQueue.isNotEmpty() || diagonalQueue.isNotEmpty()) && result.size < 128) {
            val current = if (faceQueue.isNotEmpty()) faceQueue.removeFirst() else diagonalQueue.removeFirst()
            result.add(current)

            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        if (dx == 0 && dy == 0 && dz == 0) continue

                        val next = current.location.clone().add(dx.toDouble(), dy.toDouble(), dz.toDouble()).block
                        if (next in visited || next.type != targetType) continue

                        visited.add(next)

                        if ((kotlin.math.abs(dx) + kotlin.math.abs(dy) + kotlin.math.abs(dz)) == 1) {
                            faceQueue.add(next)
                        } else {
                            diagonalQueue.add(next)
                        }
                    }
                }
            }
        }
    }
}
