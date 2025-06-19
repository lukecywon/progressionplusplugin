package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.listeners.weapons.legendary.BoomerangBladeKeys
import org.bukkit.Material
import org.bukkit.entity.Snowball
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.metadata.FixedMetadataValue
import java.util.*

object BoomerangBlade : CustomItem("boomerang_blade", Rarity.LEGENDARY) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.TRIDENT)
        val meta = item.itemMeta
        meta.setDisplayName("§6Boomerang Blade")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun launchBoomerang(player: Player) {
        val projectile = player.launchProjectile(org.bukkit.entity.Arrow::class.java).apply {
            shooter = player
            customName = "BoomerangBlade"
            isCustomNameVisible = false
            setMetadata("boomerang_blade", FixedMetadataValue(ProgressionPlus.getPlugin(), true))
            isCritical = false
            isSilent = true
            damage = 2.0
            setGravity(false)
            pickupStatus = org.bukkit.entity.AbstractArrow.PickupStatus.DISALLOWED
        }


        val direction = player.eyeLocation.direction.normalize()
        val start = player.eyeLocation.toVector()
        val origin = player.location
        projectile.velocity = direction.multiply(1.5)

        val uuid = UUID.randomUUID()
        projectile.persistentDataContainer.set(
            BoomerangBladeKeys.isBoomerang, PersistentDataType.STRING, uuid.toString()
        )

        object : BukkitRunnable() {
            var returning = false
            override fun run() {
                if (projectile.isDead || !projectile.isValid) {
                    cancel()
                    return
                }

                val distance = projectile.location.toVector().distance(start)
                if (distance >= 10.0 || projectile.location.block.type.isSolid) {
                    returning = true
                }

                if (returning) {
                    val toPlayer = origin.toVector().subtract(projectile.location.toVector()).normalize()
                    projectile.velocity = toPlayer.multiply(1.5)

                    if (projectile.location.distance(origin) < 1.5) {
                        projectile.remove()
                        cancel()
                    }
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 2L)
    }
}
