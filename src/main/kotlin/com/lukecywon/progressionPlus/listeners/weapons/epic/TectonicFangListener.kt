package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.epic.TectonicFang
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class TectonicFangListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()
    private val lastFaceMap = mutableMapOf<Player, BlockFace>()

    private val veinMineable = setOf(
        Material.STONE, Material.DEEPSLATE, Material.TUFF, Material.GRANITE, Material.DIORITE, Material.ANDESITE,
        Material.COBBLESTONE, Material.INFESTED_STONE, Material.INFESTED_COBBLESTONE, Material.INFESTED_DEEPSLATE,
        Material.INFESTED_CHISELED_STONE_BRICKS, Material.INFESTED_CRACKED_STONE_BRICKS, Material.INFESTED_MOSSY_STONE_BRICKS,
        Material.INFESTED_STONE_BRICKS,
        Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE, Material.GOLD_ORE,
        Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE,
        Material.DEEPSLATE_COAL_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_REDSTONE_ORE, Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE
    )

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!TectonicFang.isTectonicFang(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        val newSize = TectonicFang.cycleVeinSize(item)
        player.sendActionBar(Component.text("Tectonic Fang vein size set to $newSize"))

        if (e.blockFace != null) {
            lastFaceMap[player] = e.blockFace
        }
    }

    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val block = e.block
        val item = player.inventory.itemInMainHand

        if (!TectonicFang.isTectonicFang(item)) return
        if (block.type !in veinMineable) return

        val targetType = block.type
        val max = TectonicFang.getVeinSize(item)
        val visited = mutableSetOf<Block>()
        val toBreak = mutableListOf<Block>()

        val directionVector = lastFaceMap[player]?.oppositeFace?.direction ?: Vector(0, 0, 1)

        e.isCancelled = true

        val isOre = block.type.name.endsWith("_ORE")

        if (isOre) {
            collectBFSBlocks(block, targetType, visited, toBreak, max)
        } else {
            collectSameTypeBlocks(block, targetType, visited, toBreak, max, directionVector)
        }

        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i >= toBreak.size) {
                    cancel()
                    return
                }

                val b = toBreak[i]
                b.breakNaturally(item)
                player.playSound(b.location, Sound.BLOCK_STONE_BREAK, 1f, 1f)
                i++
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    private fun collectSameTypeBlocks(
        start: Block,
        targetType: Material,
        visited: MutableSet<Block>,
        result: MutableList<Block>,
        max: Int,
        faceDirection: Vector
    ) {
        val origin = start.location
        val centerX = origin.blockX
        val centerY = origin.blockY
        val centerZ = origin.blockZ

        val cubeQueue = ArrayDeque<Block>()
        val postCubeQueue = ArrayDeque<Block>()

        visited.add(start)
        cubeQueue.add(start)

        // Step 1: Fill ideal 5×5×5 cube
        while (cubeQueue.isNotEmpty() && result.size < max) {
            val current = cubeQueue.removeFirst()
            result.add(current)

            for (dx in -1..1) {
                for (dy in -1..1) {
                    for (dz in -1..1) {
                        val next = current.location.clone().add(dx.toDouble(), dy.toDouble(), dz.toDouble()).block
                        if (next in visited || next.type != targetType) continue

                        visited.add(next)

                        val cx = next.x - centerX
                        val cy = next.y - centerY
                        val cz = next.z - centerZ

                        if (kotlin.math.abs(cx) <= 2 && kotlin.math.abs(cy) <= 2 && kotlin.math.abs(cz) <= 2) {
                            cubeQueue.add(next)
                        } else {
                            postCubeQueue.add(next)
                        }
                    }
                }
            }
        }

        if (result.size >= max) return

        // Step 2: Expand from opposite face
        val sorted = postCubeQueue.sortedBy {
            val dx = it.x - centerX
            val dy = it.y - centerY
            val dz = it.z - centerZ
            -(dx * faceDirection.blockX + dy * faceDirection.blockY + dz * faceDirection.blockZ)
        }

        for (block in sorted) {
            if (result.size >= max) break
            result.add(block)
        }
    }

    private fun collectBFSBlocks(
        start: Block,
        targetType: Material,
        visited: MutableSet<Block>,
        result: MutableList<Block>,
        max: Int
    ) {
        val faceQueue = ArrayDeque<Block>()
        val diagonalQueue = ArrayDeque<Block>()

        faceQueue.add(start)
        visited.add(start)

        while ((faceQueue.isNotEmpty() || diagonalQueue.isNotEmpty()) && result.size < max) {
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
