package com.lukecywon.progressionPlus.listeners.utility.epic

import com.lukecywon.progressionPlus.items.utility.epic.BuilderWand
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.*
import kotlin.collections.ArrayDeque

class BuilderWandListener : Listener {
    private val itemId = "builder_wand"
    private val cooldownMillis = 500L
    private val undoCooldown = mutableMapOf<UUID, Long>()
    private val undoCooldownMillis = 250L
    private val maxBlocks = 64
    private val maxUndo = 3
    private val undoStacks = mutableMapOf<UUID, ArrayDeque<List<Block>>>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.hand != EquipmentSlot.HAND) return
        if (e.action != Action.RIGHT_CLICK_BLOCK) return
        if (!BuilderWand.isBuilderWand(item)) return

        val playerId = player.uniqueId

        // Handle UNDO
        if (player.isSneaking) {
            val now = System.currentTimeMillis()
            val lastUse = undoCooldown[playerId] ?: 0L
            if (now - lastUse < undoCooldownMillis) return

            undoCooldown[playerId] = now

            val undoStack = undoStacks[playerId]
            if (undoStack != null && undoStack.isNotEmpty()) {
                val lastPlacement = undoStack.removeLast()
                for (block in lastPlacement) {
                    block.type = Material.AIR
                }
                player.playSound(player.location, Sound.BLOCK_GRAVEL_BREAK, 1f, 1f)
                player.sendMessage("§cUndo successful!")
            } else {
                player.sendMessage("§7Nothing to undo!")
            }

            e.isCancelled = true
            return
        }

        if (CustomItem.isOnCooldown(itemId, playerId)) return

        val clickedBlock = e.clickedBlock ?: return
        val face = e.blockFace
        val material = clickedBlock.type
        val start = clickedBlock.getRelative(face)

        val isCreative = player.gameMode == GameMode.CREATIVE
        var available = if (isCreative) Int.MAX_VALUE else countBlocks(player, material)

        val visited = mutableSetOf<Block>()
        val queue = ArrayDeque<Block>()
        val placedBlocks = mutableListOf<Block>()
        queue.add(start)

        var placedCount = 0

        while (queue.isNotEmpty() && placedCount < maxBlocks && available > 0) {
            val current = queue.removeFirst()
            if (!visited.add(current)) continue

            val support = current.getRelative(face.oppositeFace)
            if (current.type != Material.AIR || support.type != material) continue

            current.type = material
            placedBlocks.add(current)
            placedCount++
            available--

            for ((dx, dy, dz) in expansionOffsets(face)) {
                queue.add(current.getRelative(dx, dy, dz))
            }
        }

        if (placedCount > 0) {
            player.playSound(player.location, Sound.BLOCK_STONE_PLACE, 1f, 1f)
            CustomItem.setCooldown(itemId, playerId, cooldownMillis)

            // Save for undo
            val stack = undoStacks.getOrPut(playerId) { ArrayDeque() }
            if (stack.size >= maxUndo) stack.removeFirst()
            stack.addLast(placedBlocks)

            if (!isCreative) removeBlocks(player, material, placedCount)
        } else if (!isCreative && available <= 0) {
            player.sendMessage("§cYou don't have enough of that block to place!")
        }

        e.isCancelled = true
    }

    private fun expansionOffsets(face: BlockFace): List<Triple<Int, Int, Int>> {
        return when (face) {
            BlockFace.UP, BlockFace.DOWN -> listOf(
                Triple(1, 0, 0), Triple(-1, 0, 0), Triple(0, 0, 1), Triple(0, 0, -1),
                Triple(1, 0, 1), Triple(1, 0, -1), Triple(-1, 0, 1), Triple(-1, 0, -1)
            )
            BlockFace.NORTH, BlockFace.SOUTH -> listOf(
                Triple(1, 0, 0), Triple(-1, 0, 0), Triple(0, 1, 0), Triple(0, -1, 0),
                Triple(1, 1, 0), Triple(-1, 1, 0), Triple(1, -1, 0), Triple(-1, -1, 0)
            )
            BlockFace.EAST, BlockFace.WEST -> listOf(
                Triple(0, 1, 0), Triple(0, -1, 0), Triple(0, 0, 1), Triple(0, 0, -1),
                Triple(0, 1, 1), Triple(0, 1, -1), Triple(0, -1, 1), Triple(0, -1, -1)
            )
            else -> emptyList()
        }
    }

    private fun countBlocks(player: org.bukkit.entity.Player, material: Material): Int {
        return player.inventory.contents.filterNotNull().filter { it.type == material }.sumOf { it.amount }
    }

    private fun removeBlocks(player: org.bukkit.entity.Player, material: Material, amountToRemove: Int) {
        var remaining = amountToRemove
        val contents = player.inventory.contents

        for (i in contents.indices) {
            val item = contents[i] ?: continue
            if (item.type != material) continue

            if (item.amount <= remaining) {
                remaining -= item.amount
                contents[i] = null
            } else {
                item.amount -= remaining
                break
            }

            if (remaining <= 0) break
        }

        player.inventory.contents = contents
        player.updateInventory()
    }
}
