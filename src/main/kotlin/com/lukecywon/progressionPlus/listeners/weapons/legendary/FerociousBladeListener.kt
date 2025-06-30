package com.lukecywon.progressionPlus.listeners.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.epic.FerociousBlade
import org.bukkit.Bukkit
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class FerociousBladeListener : Listener {
    @EventHandler
    fun onHit(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? Player ?: return
        val target = event.entity as? LivingEntity ?: return
        val weapon = damager.inventory.itemInMainHand

        if (!FerociousBlade.isThisItem(weapon)) return
        val chance = Math.random()

        // 50% chance to double hit
        if (chance < 0.5) {
            // Delay second hit slightly to avoid visual overlap
            Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
                target.noDamageTicks = 0
                target.damage(event.damage, damager)
                target.world.playSound(target.location, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1f, 1.5f)
                target.world.spawnParticle(Particle.SWEEP_ATTACK, target.location.add(0.0, target.height / 2, 0.0), 5, 0.5, 0.5, 0.5, 0.01)
            }, 1L)
        }
    }

}