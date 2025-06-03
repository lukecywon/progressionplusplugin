package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.items.VenomDagger
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class VenomDaggerListener : Listener {
    private val cooldowns = mutableMapOf<UUID, Long>()  // player UUID → next allowed time (ms)

    @EventHandler
    fun onHit(e: EntityDamageByEntityEvent) {
        val attacker = e.damager as? Player ?: return
        val target = e.entity as? LivingEntity ?: return

        val item = attacker.inventory.itemInMainHand
        if (!VenomDagger.isVenomDagger(item)) return

        val now = System.currentTimeMillis()
        val nextAllowed = cooldowns[attacker.uniqueId] ?: 0

        if (now >= nextAllowed) {
            cooldowns[attacker.uniqueId] = now + 7000  // 7s cooldown
            target.addPotionEffect(PotionEffect(PotionEffectType.POISON, 60, 0)) // 3s Poison I
            target.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 60, 0)) // 3s Weakness I
            attacker.sendActionBar("§aVenom effects applied!")
        }
    }
}
