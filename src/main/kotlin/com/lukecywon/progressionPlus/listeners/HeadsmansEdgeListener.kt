package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.weapons.rare.HeadsmansEdge
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class HeadsmansEdgeListener : Listener {

    @EventHandler
    fun onKillWithEdge(event: EntityDeathEvent) {
        val killer = event.entity.killer ?: return
        val weapon = killer.inventory.itemInMainHand
        if (!HeadsmansEdge.isThisItem(weapon)) return
        if (Math.random() > 0.20) return  // 20% chance

        val head = when (event.entity.type) {
            EntityType.ZOMBIE -> ItemStack(Material.ZOMBIE_HEAD)
            EntityType.SKELETON -> ItemStack(Material.SKELETON_SKULL)
            EntityType.CREEPER -> ItemStack(Material.CREEPER_HEAD)
            EntityType.WITHER_SKELETON -> ItemStack(Material.WITHER_SKELETON_SKULL)
            EntityType.PIGLIN -> ItemStack(Material.PIGLIN_HEAD)
            EntityType.PIGLIN_BRUTE -> ItemStack(Material.PIGLIN_HEAD)
            EntityType.PLAYER -> {
                val skull = ItemStack(Material.PLAYER_HEAD)
                val meta = skull.itemMeta as SkullMeta
                meta.owningPlayer = event.entity as? Player
                skull.itemMeta = meta
                skull
            }
            else -> return
        }

        event.drops.add(head)
    }
}
