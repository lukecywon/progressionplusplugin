package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.*
import com.lukecywon.progressionPlus.items.utility.common.*
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class BannerListener : Listener {

    private val cooldownMillis = 5 * 60 * 1000L // 5 minutes
    private val itemId = "banner_ability"

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = e.item ?: return

        if (e.hand != EquipmentSlot.HAND) return
        if (
            !SpeedBanner.isSpeedBanner(item) &&
            !HasteBanner.isHasteBanner(item) &&
            !RegenBanner.isRegenBanner(item) &&
            !JumpBanner.isJumpBanner(item) &&
            !AbsorptionBanner.isAbsorptionBanner(item)
        ) return

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cBanners are on cooldown. Try again in ${minutes}m ${seconds}s.")
            e.isCancelled = true
            return
        }

        val (type, effect) = when {
            SpeedBanner.isSpeedBanner(item) -> "§Speed Banner" to PotionEffect(PotionEffectType.SPEED, 20 * 30, 0)
            HasteBanner.isHasteBanner(item) -> "§eHaste Banner" to PotionEffect(PotionEffectType.HASTE, 20 * 30, 0)
            RegenBanner.isRegenBanner(item) -> "§dRegen Banner" to PotionEffect(PotionEffectType.REGENERATION, 20 * 30, 0)
            JumpBanner.isJumpBanner(item) -> "§aJump Banner" to PotionEffect(PotionEffectType.JUMP_BOOST, 20 * 30, 1)
            AbsorptionBanner.isAbsorptionBanner(item) -> "§6Absorption Banner" to PotionEffect(PotionEffectType.ABSORPTION, 20 * 30, 0)
            else -> return
        }

        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)
        e.isCancelled = true

        val radius = 5.0
        val targets = player.getNearbyEntities(radius, radius, radius).filterIsInstance<Player>() + player

        for (p in targets) {
            p.addPotionEffect(effect)
        }

        player.sendMessage("$type activated! Everyone nearby feels its effect.")
        showParticleCircle(player, radius)
    }

    private fun showParticleCircle(player: Player, radius: Double) {
        val center = player.location.clone().add(0.0, 0.1, 0.0)

        object : BukkitRunnable() {
            var ticks = 0
            override fun run() {
                if (ticks++ > 40) {
                    cancel()
                    return
                }

                val points = 50
                for (i in 0 until points) {
                    val angle = 2 * Math.PI * i / points
                    val x = center.x + radius * Math.cos(angle)
                    val z = center.z + radius * Math.sin(angle)
                    val loc = center.clone().apply {
                        this.x = x
                        this.z = z
                    }
                    player.world.spawnParticle(Particle.ELECTRIC_SPARK, loc, 1)
                }
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 1L)
    }

    @EventHandler
    fun onBannerPlace(e: BlockPlaceEvent) {
        val item = e.itemInHand ?: return
        if (
            HasteBanner.isHasteBanner(item) ||
            RegenBanner.isRegenBanner(item) ||
            JumpBanner.isJumpBanner(item) ||
            AbsorptionBanner.isAbsorptionBanner(item)
        ) {
            e.isCancelled = true
            e.player.sendMessage("§cYou can't place that banner!")
        }
    }
}
