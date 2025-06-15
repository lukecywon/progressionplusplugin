package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.utility.rare.ContainmentSigil
import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ContainmentSigilListener : Listener {

    private val markedMobs: MutableMap<UUID, Long> = mutableMapOf()
    private val markDurationMillis = 2 * 60 * 1000L  // 2 minutes

    @EventHandler
    fun onSigilHit(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        val entity = event.entity
        val item = player.inventory.itemInMainHand

        if (!ContainmentSigil.isThisItem(item)) return

        // Block invalid targets
        if (entity is Player || entity is EnderDragon || entity is Wither || entity is ElderGuardian || entity is Warden || entity.scoreboardTags.contains("unmarkable")) {
            player.sendMessage("§cYou cannot mark this entity.")
            return
        }

        // Check if already marked
        if (markedMobs.containsKey(entity.uniqueId)) {
            player.sendMessage("§eThis creature is already marked.")
            return
        }

        // Mark it
        val uuid = entity.uniqueId
        markedMobs[uuid] = System.currentTimeMillis() + markDurationMillis

        if (entity is LivingEntity) {
            entity.addPotionEffect(
                PotionEffect(PotionEffectType.GLOWING, (markDurationMillis / 50).toInt(), 0, false, false)
            )
        }

        entity.world.spawnParticle(Particle.ENCHANTED_HIT, entity.location.add(0.0, 1.0, 0.0), 30, 0.5, 1.0, 0.5, 0.0)
        player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1.2f)
        player.sendMessage("§dThe creature has been marked for containment.")
        item.amount = item.amount - 1
        event.isCancelled = true

        // Remove glow after time
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            val refreshed = Bukkit.getEntity(uuid) as? LivingEntity ?: return@Runnable
            if (markedMobs.containsKey(uuid)) {
                refreshed.removePotionEffect(PotionEffectType.GLOWING)
                markedMobs.remove(uuid)
            }
        }, markDurationMillis / 50)
    }

    @EventHandler
    fun onMarkedMobDeath(event: EntityDeathEvent) {
        val entity = event.entity
        val uuid = entity.uniqueId

        if (!markedMobs.containsKey(uuid)) return
        val expiresAt = markedMobs.remove(uuid) ?: return
        if (System.currentTimeMillis() > expiresAt) return

        // Prevent XP and regular drops
        event.droppedExp = 0
        event.drops.clear()

        // Add spawn egg only
        val eggMaterial = Material.matchMaterial("${entity.type}_SPAWN_EGG") ?: return
        event.drops.add(ItemStack(eggMaterial))
    }
}
