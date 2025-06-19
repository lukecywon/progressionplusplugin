package com.lukecywon.progressionPlus.listeners.loot

import com.lukecywon.progressionPlus.items.component.TideCrystal
import com.lukecywon.progressionPlus.items.progression.NetherEye
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class ElderGuardianDropListener : Listener {

    @EventHandler
    fun onElderGuardianDeath(event: EntityDeathEvent) {
        val entity = event.entity

        // Only affect Elder Guardians
        if (entity.type != EntityType.ELDER_GUARDIAN) return

        // Optionally check if killed by a player
        if (entity.killer != null) {
            val item = TideCrystal.createItemStack()
            event.drops.add(item)
        }
    }
}
