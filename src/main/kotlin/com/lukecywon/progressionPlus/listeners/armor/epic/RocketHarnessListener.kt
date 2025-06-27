package com.lukecywon.progressionPlus.listeners.armor.epic

import com.lukecywon.progressionPlus.items.armor.epic.RocketHarness
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent
import io.papermc.paper.event.entity.EntityMoveEvent
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot

class RocketHarnessListener : Listener {
    @EventHandler
    fun onMobHarnessChange(event: EntityEquipmentChangedEvent) {
        val entity = event.entity
        if (entity.type != EntityType.HAPPY_GHAST) return
        val attr = entity.getAttribute(Attribute.FLYING_SPEED) ?: return

        // Check if the SADDLE slot changed to HarnessOfTears
        if(!RocketHarness.isThisItem(entity.equipment?.getItem(EquipmentSlot.BODY))) {
            // Remove speed on harness unequipped
            attr.modifiers.find { it.name == "harness_speed" }?.let {
                attr.removeModifier(it)
            }
            return
        }

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

    @EventHandler
    fun onEntityMoveEvent(event: EntityMoveEvent) {
        val entity = event.entity
        if (entity.type != EntityType.HAPPY_GHAST) return

        // Check if the BODY slot changed to HarnessOfTears
        if(!RocketHarness.isThisItem(entity.equipment?.getItem(EquipmentSlot.BODY))) return

        // Spawn particle at the entity's location
        val location = entity.location.clone().add(0.0, 1.5, 0.0)

        entity.world.spawnParticle(
            Particle.FLAME,
            location,
            5,
            0.2, 0.2, 0.2,
            0.01
        )
    }

    @EventHandler
    fun onPlayerMoveWhileRiding(event: PlayerMoveEvent) {
        val player = event.player
        val vehicle = player.vehicle ?: return

        // Check if the vehicle is a specific entity
        if (vehicle.type == EntityType.HAPPY_GHAST) {
            val entity = vehicle as? LivingEntity ?: return
            // Optional: only detect if the player is steering (could check velocity)
            if (event.from.distanceSquared(event.to) > 0.001) {
                if(!RocketHarness.isThisItem(entity.equipment?.getItem(EquipmentSlot.BODY))) return

                val location = entity.location.clone().add(0.0, 3.0, 0.0)

                entity.world.spawnParticle(
                    Particle.FLAME,
                    location,
                    5,
                    0.2, 0.2, 0.2,
                    0.01
                )

                entity.world.playSound(
                    entity.location,
                    Sound.ENTITY_BLAZE_BURN,
                    SoundCategory.HOSTILE,
                    1.0f,
                    0.5f
                )
            }
        }
    }
}