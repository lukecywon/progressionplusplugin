package com.lukecywon.progressionPlus.listeners.armor

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class GlassArmorListener : Listener {

    private val plugin = ProgressionPlus.getPlugin()
    private val armorKey = NamespacedKey(plugin, "glass_armor")
    private val hitKey = NamespacedKey(plugin, "glass_hits")

    private fun isGlassArmor(item: ItemStack?): Boolean {
        val meta = item?.itemMeta ?: return false
        return meta.persistentDataContainer.has(armorKey, PersistentDataType.BYTE)
    }

    private fun isWearingFullGlassSet(player: Player): Boolean {
        return player.inventory.armorContents.all { isGlassArmor(it) }
    }

    @EventHandler
    fun onPlayerDamaged(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (event.damage <= 3.0) return // Only block if damage > 1.5 hearts

        if (!isWearingFullGlassSet(player)) return

        val data = player.persistentDataContainer
        val hits = data.getOrDefault(hitKey, PersistentDataType.INTEGER, 0)

        if (hits < 4) {
            event.isCancelled = true
            data.set(hitKey, PersistentDataType.INTEGER, hits + 1)

            if (hits + 1 == 4) {
                // 4th hit absorbed, then shatter
                player.inventory.armorContents = arrayOf(null, null, null, null)
                data.remove(hitKey)
                player.sendMessage("§cYour Glass Armor shattered!")
                player.world.playSound(player.location, org.bukkit.Sound.BLOCK_GLASS_BREAK, 1f, 1f)
                val blockData = Material.GLASS.createBlockData()
                player.world.spawnParticle(Particle.BLOCK, player.location.add(0.0, 1.0, 0.0), 30, 0.4, 0.8, 0.4, 0.02, blockData)
            } else {
                player.sendMessage("§bYour Glass Armor absorbed the blow! (${hits + 1}/4)")
            }
        }

    }
}
