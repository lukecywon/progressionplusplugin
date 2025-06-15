package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.utility.legendary.NetherEye
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.loot.LootTables

class StructureLootListener : Listener {

    @EventHandler
    fun onLootGenerate(event: LootGenerateEvent) {
        val lootTable = event.lootTable.key

        when (lootTable) {
            LootTables.DESERT_PYRAMID.key -> handleDesertTemple(event)
            LootTables.WOODLAND_MANSION.key -> handleWoodlandMansion(event)
            LootTables.ANCIENT_CITY.key -> handleAncientCity(event)
        }
    }

    private fun handleDesertTemple(event: LootGenerateEvent) {
        val chance = 0.5  // 50%
        if (Math.random() <= chance) {
            event.loot.add(NetherEye.createItemStack())
        }
    }

    private fun handleWoodlandMansion(event: LootGenerateEvent) {
        val chance = 1  // 100%
        if (Math.random() <= chance) {
            event.loot.add(NetherEye.createItemStack())  // assuming this exists
        }
    }

    private fun handleAncientCity(event: LootGenerateEvent) {
        val chance = 0.2  // 20%
        if (Math.random() <= chance) {
            event.loot.add(NetherEye.createItemStack())
        }
    }
}
