package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.utility.legendary.NetherEye
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
            val item = NetherEye.createItemStack()
            event.drops.add(item)
        }
    }
}
