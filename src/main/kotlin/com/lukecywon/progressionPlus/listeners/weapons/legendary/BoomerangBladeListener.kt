package com.lukecywon.progressionPlus.listeners.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.weapons.legendary.BoomerangBlade
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Arrow
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object BoomerangBladeKeys {
    val isBoomerang = org.bukkit.NamespacedKey(ProgressionPlus.getPlugin(), "boomerang_blade")
}

class BoomerangBladeListener : Listener {

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand
        if (!BoomerangBlade.isThisItem(item)) return

        e.isCancelled = true
        BoomerangBlade.launchBoomerang(player)
    }

    @EventHandler
    fun onBoomerangHit(e: ProjectileHitEvent) {
        val projectile = e.entity as? Arrow ?: return
        val key = BoomerangBladeKeys.isBoomerang
        if (!projectile.persistentDataContainer.has(key, PersistentDataType.STRING)) return

        val hit = e.hitEntity as? LivingEntity ?: return

        // ✅ Manually deal 1 heart damage
        val shooter = projectile.shooter
        if (shooter is org.bukkit.entity.Entity && shooter != hit) {
            hit.damage(2.0, shooter)
        }

        val attr = hit.getAttribute(Attribute.ARMOR) ?: return
        val currentArmor = attr.value
        if (currentArmor <= 0.0) return

        val shredAmount = -1.5
        val modifierId = UUID.randomUUID()
        val modifier = AttributeModifier(
            modifierId,
            "boomerang_shred_${modifierId}",
            shredAmount,
            AttributeModifier.Operation.ADD_NUMBER
        )
        attr.addModifier(modifier)

        object : BukkitRunnable() {
            override fun run() {
                attr.removeModifier(modifier)
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 20L * 4)
    }
}
