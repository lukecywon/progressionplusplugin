package com.lukecywon.progressionPlus.listeners.utility.rare

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.utility.rare.EarthSplitter
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class EarthSplitterListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()

    private val shovelables = setOf(
        Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT,
        Material.ROOTED_DIRT, Material.MUD, Material.MYCELIUM, Material.PODZOL,
        Material.SAND, Material.RED_SAND, Material.GRAVEL, Material.CLAY,
        Material.SOUL_SAND, Material.SOUL_SOIL, Material.SNOW, Material.SNOW_BLOCK,
        Material.POWDER_SNOW, Material.FARMLAND
    )

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val block = e.block
        val item = player.inventory.itemInMainHand

        if (!EarthSplitter.isThisItem(item)) return
        if (block.type !in shovelables) return

        val targetType = block.type
        e.isCancelled = true

        val visited = mutableSetOf<Block>()
        val result = mutableListOf<Block>()
        collectSameTypeBlocks(block, targetType, visited, result)

        val blocks = result.take(EarthSplitter.getVeinSize(item))

        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i >= blocks.size) {
                    cancel()
                    return
                }

                val b = blocks[i]
                b.breakNaturally(item)
                player.playSound(b.location, Sound.BLOCK_SAND_BREAK, 1f, 1f)
                i++
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!EarthSplitter.isThisItem(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true
        val newSize = EarthSplitter.cycleVeinSize(item)
        player.sendActionBar(Component.text("Earthsplitter vein size set to $newSize"))
    }

    private fun collectSameTypeBlocks(
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

            for (dx in -2..2) {
                for (dy in -2..2) {
                    for (dz in -2..2) {
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
