package com.lukecywon.progressionPlus.listeners.weapons.epic

import com.lukecywon.progressionPlus.items.weapons.epic.ExecutionerSword
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector
import java.util.*

class ExecutionerSwordListener : Listener {
    val playerCharges = mutableMapOf<UUID, Int>()

    @EventHandler
    fun onEntityDamage(event: EntityDamageByEntityEvent) {
        val player = event.damager as? Player ?: return
        if (!ExecutionerSword.isThisItem(player.inventory.itemInMainHand)) return

        val uuid = player.uniqueId
        val current = playerCharges.getOrDefault(uuid, 0)
        if (current < 5) {
            playerCharges[uuid] = current + 1
            player.sendActionBar(Component.text("⚡ Cleave Charge: ${current + 1}", NamedTextColor.YELLOW))
        }
    }

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (!ExecutionerSword.isThisItem(item)) return
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return

        val location = player.location
        val direction = location.direction.normalize()
        val world = player.world

        val uuid = player.uniqueId
        val charges = playerCharges.getOrDefault(uuid, 0)

        if (charges <= 0) {
            player.sendMessage(Component.text("❌ Not enough charge!", NamedTextColor.RED))
            return
        }

        // 5 block range, 100° angle cone
        val range = 5.0
        val angle = Math.toRadians(100.0)

        for (entity in world.getNearbyLivingEntities(location, range)) {
            if (entity == player) continue

            val toEntity = entity.location.toVector().subtract(player.location.toVector()).normalize()
            val dot = direction.dot(toEntity)

            if (dot > Math.cos(angle / 2)) {
                if (entity is LivingEntity) {
                    entity.noDamageTicks = 0 // Ignore damage cooldown
                    entity.damage(5.0, player)
                }
            }
        }

        // 💥 Calculate particle location: 1.5 blocks in front of player
        val particleLocation = player.eyeLocation.clone().add(direction.multiply(1.5))
        particleLocation.y = player.location.y + 1.0  // Make it chest-level

        // Spawn sweep particle at that point
        spawnCleaveEffect(player)
        world.playSound(player.location, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f)
        world.playSound(player.location, Sound.BLOCK_ANVIL_HIT, 1f, 1f)

        playerCharges[uuid] = charges - 1
        player.sendActionBar(Component.text("⚡ Cleave Charge: ${charges - 1}", NamedTextColor.YELLOW))

        event.isCancelled = true
    }

    fun spawnCleaveEffect(player: Player) {
        val world = player.world
        val origin = player.location.clone()
        origin.y += 1.0 // Slightly above ground level

        val direction = origin.direction.normalize()
        val angleSpread = 60 // degrees of arc
        val count = 10       // number of particles in the arc
        val radius = 2.0     // how far in front they appear

        for (i in 0 until count) {
            val angle = Math.toRadians(-angleSpread / 2 + (angleSpread.toDouble() / count) * i)
            val x = direction.x * Math.cos(angle) - direction.z * Math.sin(angle)
            val z = direction.x * Math.sin(angle) + direction.z * Math.cos(angle)

            val offset = Vector(x, 0.0, z).normalize().multiply(radius)
            val particleLocation = origin.clone().add(offset)
            world.spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 1)
        }
    }
}