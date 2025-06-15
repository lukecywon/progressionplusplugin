package com.lukecywon.progressionPlus.listeners.weapons.rare

import org.bukkit.entity.Trident
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class TribalSpearListener : Listener {
    @EventHandler
    fun onTridentHit(event: EntityDamageByEntityEvent) {
        val trident = event.damager as? Trident ?: return

        // Only modify thrown tridents (skip melee attacks)
        if (trident.isOnGround || trident.isInBlock) return

        event.damage = 5.0
    }
}