package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import java.util.*

object EchoGun : CustomItem("echo_gun", Rarity.LEGENDARY) {
    private val cooldowns = mutableMapOf<UUID, Long>()

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ECHO_SHARD)
        val meta = item.itemMeta


        meta.displayName(
            Component.text()
            .append(Component.text("=").decorate(TextDecoration.OBFUSCATED).color(getRarity().color))
            .append(Component.text("Echo Gun").color(getRarity().color))
            .append(Component.text("=").decorate(TextDecoration.OBFUSCATED).color(getRarity().color))
            .build()
        )

        meta.lore(listOf(
            Component.text("Carved from the chest of an immortal warden.").color(NamedTextColor.DARK_RED),
            Component.text("Feel it's heartbeat in your hands...").color(NamedTextColor.DARK_RED)
        ))

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "damage"),
            10.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "echo_gun")

        item.itemMeta = meta

        return applyMeta(item)
    }

    fun shootSonicBoom(player: Player) {
        val chargeTicks = 20L // 2 seconds (20 ticks per second)
        val world = player.world
        val location = player.location

        val cooldownTime = 5_000L // 5 seconds in milliseconds
        val now = System.currentTimeMillis()
        val uuid = player.uniqueId

        // Check if player is on cooldown
        val lastUsed = cooldowns[uuid]
        if (lastUsed != null && now - lastUsed < cooldownTime) {
            val secondsLeft = (cooldownTime - (now - lastUsed)) / 1000.0
            player.sendActionBar(
                Component.text("⏳ Ability on cooldown: %.1f seconds left".format(secondsLeft)).color(
                    NamedTextColor.RED))

            return
        }

        cooldowns[uuid] = now


        world.playSound(location, Sound.BLOCK_BEACON_POWER_SELECT, 1f, 1.5f)
        world.spawnParticle(Particle.END_ROD, player.eyeLocation, 30, 0.5, 0.5, 0.5, 0.01)
        player.sendActionBar(Component.text("⚡ Charging... ⚡").color(NamedTextColor.YELLOW))

        // Schedule actual firing after chargeTicks
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            val direction = player.eyeLocation.direction.normalize()
            val start = player.eyeLocation

            for (i in 1..40) {
                val point = start.clone().add(direction.clone().multiply(i))
                world.spawnParticle(Particle.SONIC_BOOM, point, 1, 0.0, 0.0, 0.0, 0.0)

                val nearby = world.getNearbyEntities(point, 1.0, 1.0, 1.0)
                for (entity in nearby) {
                    if (entity != player && entity is LivingEntity) {
                        entity.damage(30.0, player)
                        world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
                        world.playSound(point, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
                    }
                }
            }

            // Sound effect if nothing hit
            world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)

        }, chargeTicks)
    }
}