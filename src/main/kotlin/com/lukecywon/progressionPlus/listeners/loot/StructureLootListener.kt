package com.lukecywon.progressionPlus.listeners.loot

import com.lukecywon.progressionPlus.items.armor.legendary.TwilightCrown
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.items.component.SunscorchedEmber
import com.lukecywon.progressionPlus.items.component.TwistedRoot
import com.lukecywon.progressionPlus.items.progression.NetherEye
import com.lukecywon.progressionPlus.items.weapons.rare.OldKingsBlade
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
            LootTables.END_CITY_TREASURE.key -> handleEndCityShip(event)
        }
    }

    private fun handleDesertTemple(event: LootGenerateEvent) {
        val chance = 0.5  // 50%
        if (Math.random() <= chance) {
            event.loot.add(SunscorchedEmber.createItemStack())
        }
        if (Math.random() <= chance) {
            event.loot.add(OldKingsBlade.createItemStack())
        }
    }

    private fun handleWoodlandMansion(event: LootGenerateEvent) {
        val chance = 1  // 100%
        if (Math.random() <= chance) {
            event.loot.add(TwistedRoot.createItemStack())  // assuming this exists
        }
    }

    private fun handleAncientCity(event: LootGenerateEvent) {
        val chance = 0.2  // 20%
        if (Math.random() <= chance) {
            event.loot.add(EchoCore.createItemStack())
        }
    }

    private fun handleEndCityShip(event: LootGenerateEvent) {
        val chance = 0.1  // 10% chance
        if (Math.random() <= chance) {
            event.loot.add(TwilightCrown.createItemStack())
        }
    }

}
