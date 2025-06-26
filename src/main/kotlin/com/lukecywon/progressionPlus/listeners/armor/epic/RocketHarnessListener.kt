package com.lukecywon.progressionPlus.listeners.armor.epic

import com.lukecywon.progressionPlus.items.armor.epic.RocketHarness
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlot

class RocketHarnessListener : Listener {
    @EventHandler
    fun onMobHarnessChange(event: EntityEquipmentChangedEvent) {
        val entity = event.entity
        if (entity.type != EntityType.HAPPY_GHAST) return

        // Check if the SADDLE slot changed to HarnessOfTears
        if(!RocketHarness.isThisItem(entity.equipment?.getItem(EquipmentSlot.BODY))) return

        val attr = entity.getAttribute(Attribute.FLYING_SPEED) ?: return

        // Remove old speed modifier if it exists
        attr.modifiers.find { it.name == "harness_speed" }?.let {
            attr.removeModifier(it)
        }

        val speedBoost = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "harness_speed"),
            0.05,
            AttributeModifier.Operation.ADD_NUMBER
        )
        attr.addModifier(speedBoost)

    }
}