package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.VenomDagger
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VenomDaggerListener : Listener {
    private val itemId = "venom_dagger"
    private val cooldownMillis = 7_000L

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val attacker = e.damager as? Player ?: return
        val target = e.entity as? LivingEntity ?: return

        val item = attacker.inventory.itemInMainHand
        if (!VenomDagger.isVenomDagger(item)) return

        if (!CustomItem.isOnCooldown(itemId, attacker.uniqueId)) {
            CustomItem.setCooldown(itemId, attacker.uniqueId, cooldownMillis)
            target.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 0))   // 3s Poison I
            target.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 60, 0)) // 3s Weakness I
            attacker.sendActionBar("§aVenom effects applied!")
        }
    }
}
