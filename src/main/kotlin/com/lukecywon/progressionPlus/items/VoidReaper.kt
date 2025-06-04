package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.BlockFace
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object VoidReaper : CustomItem("void_reaper", Rarity.LEGENDARY) {
    private val soulKey = NamespacedKey("survivaltestplugin", "souls")
    private val slashCooldowns = mutableMapOf<UUID, Long>()
    private val notifiedReady = mutableSetOf<UUID>()
    private const val SLASH_COOLDOWN_MS = 10_000L

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHERITE_HOE)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("☠ Void Reaper")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(listOf(
            Component.text("§6LEGENDARY"),
            Component.text("Right-click: Unleash stored souls in an AOE blast").color(NamedTextColor.GRAY),
            Component.text("Left-click: Teleport and slash enemies you're looking at").color(NamedTextColor.DARK_GRAY),
            Component.text("Souls Stored: 0").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)
        ))

        val damageModifier = AttributeModifier(
            NamespacedKey(NamespacedKey.MINECRAFT, "damage"),
            10.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, damageModifier)

        meta.setCustomModelData(9002)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.persistentDataContainer.set(soulKey, PersistentDataType.INTEGER, 0)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun getSoulCount(item: ItemStack): Int {
        val meta = item.itemMeta ?: return 0
        return meta.persistentDataContainer.getOrDefault(soulKey, PersistentDataType.INTEGER, 0)
    }

    fun addSoul(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val current = getSoulCount(item)
        if (current >= 30) {
            // Optional: notify player in onKill() instead
            return
        }

        meta.persistentDataContainer.set(soulKey, PersistentDataType.INTEGER, (current + 1).coerceAtMost(30))
        item.itemMeta = meta
        updateLore(item)
    }

    fun consumeSouls(item: ItemStack): Int {
        val meta = item.itemMeta ?: return 0
        val souls = getSoulCount(item)
        meta.persistentDataContainer.set(soulKey, PersistentDataType.INTEGER, 0)
        item.itemMeta = meta
        updateLore(item)
        return souls
    }

    fun unleashSouls(player: Player, item: ItemStack) {
        val world = player.world
        val souls = consumeSouls(item)
        if (souls <= 0) return

        val loc = player.location
        val plugin = ProgressionPlus.getPlugin()

        // Cinematic SFX and base particles
        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1f, 0.8f)
        world.playSound(loc, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1.2f)
        world.playSound(loc, Sound.ITEM_TRIDENT_THUNDER, 1f, 1.2f)
        world.spawnParticle(Particle.EXPLOSION, loc, 1)

        // 🌌 Magic Circle
        val circleradius = 3.5
        val points = 64
        val y = loc.blockY.toDouble() + 0.05

        for (i in 0 until points) {
            val angle = 2 * Math.PI * i / points
            val x = loc.x + circleradius * Math.cos(angle)
            val z = loc.z + circleradius * Math.sin(angle)
            val pos = Location(world, x, y, z)

            world.spawnParticle(Particle.WAX_OFF, pos, 2, 0.01, 0.0, 0.01, 0.001)
            world.spawnParticle(Particle.SOUL_FIRE_FLAME, pos.clone().add(0.0, 0.1, 0.0), 3, 0.01, 0.0, 0.01, 0.001)
            if (i % 8 == 0) {
                world.spawnParticle(Particle.ENCHANTED_HIT, pos.clone().add(0.0, 0.3, 0.0), 10, 0.2, 0.0, 0.2, 0.0)
            }
        }

        // ☁ Vertical swirling rings (like a floating glyph column)
        for (layer in 0..6) {
            val spiralY = y + layer * 0.25
            for (i in 0 until 20) {
                val angle = Math.PI * 2 * i / 20 + layer * 0.5
                val spiralX = loc.x + Math.cos(angle) * (circleradius - layer * 0.4)
                val spiralZ = loc.z + Math.sin(angle) * (circleradius - layer * 0.4)
                world.spawnParticle(Particle.SCULK_SOUL, Location(world, spiralX, spiralY, spiralZ), 1, 0.0, 0.0, 0.0, 0.01)
            }
        }

        // Choose number of rings and AoE radius
        val (ringCount, radius) = when {
            souls >= 3 -> 3 to 6.0
            souls >= 2 -> 2 to 4.5
            else        -> 1 to 3.0
        }

        // Spawn magic rings
        summonFlatMagicRings(player, ringCount)

        // Center effect
        world.playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1.2f)
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0.0, 0.2, 0.0), 40, 0.5, 0.0, 0.5, 0.01)
        world.spawnParticle(Particle.DRAGON_BREATH, loc.clone().add(0.0, 0.4, 0.0), 20, 0.3, 0.0, 0.3, 0.01)

        // AoE damage
        world.getNearbyEntities(loc, radius, radius, radius).forEach {
            if (it is LivingEntity && it != player) {
                it.damage(5.0 * souls, player)
                it.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 1))
            }
        }

        // Cooldown + msg
        player.setCooldown(Material.NETHERITE_HOE, 60)
        player.sendMessage("§5Void Ritual unleashed! §d$souls §5souls consumed.")
        player.sendActionBar("§b☠ Soulburst Casted!")
    }


    fun slashTeleport(player: Player, item: ItemStack) {
        val now = System.currentTimeMillis()
        val uuid = player.uniqueId
        val last = slashCooldowns.getOrDefault(uuid, 0L)
        val elapsed = now - last

        // Still cooling down
        if (elapsed < SLASH_COOLDOWN_MS) {
            val remaining = (SLASH_COOLDOWN_MS - elapsed) / 1000
            player.sendActionBar("§cSlash cooldown: §e${remaining}s")

            // Notify once when cooldown is nearly over
            if (!notifiedReady.contains(uuid) && elapsed >= SLASH_COOLDOWN_MS - 1000) {
                player.sendActionBar("§a✔ Slash is ready!")
                notifiedReady.add(uuid)
            }
            return
        }

        val target = player.getTargetEntity(25) as? LivingEntity ?: run {
            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f)
            player.spawnParticle(Particle.SMOKE, player.eyeLocation, 10, 0.2, 0.2, 0.2, 0.01)
            return
        }

        val soulCount = getSoulCount(item)
        val damage = 4.0 + soulCount * 0.3

        val behind = target.location.clone().add(target.location.direction.multiply(-1)).apply {
            y = target.location.y
        }

        player.teleport(behind)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.7f)
        player.playSound(player.location, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1f)

        target.damage(damage, player)
        target.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 1))
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 60, 1))

        player.spawnParticle(Particle.PORTAL, player.eyeLocation, 30, 0.5, 0.5, 0.5, 0.1)
        player.spawnParticle(Particle.SWEEP_ATTACK, target.location.add(0.0, 1.0, 0.0), 5)

        player.sendActionBar("§a☠ Slash executed!")

        // Reset cooldown and readiness tracking
        slashCooldowns[uuid] = now
        notifiedReady.remove(uuid)
    }


    fun updateLore(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val count = getSoulCount(item)
        meta.lore(listOf(
            Component.text("§6LEGENDARY"),
            Component.text("Right-click: Unleash stored souls in an AOE blast").color(NamedTextColor.GRAY),
            Component.text("Left-click: Teleport and slash enemies you're looking at").color(NamedTextColor.DARK_GRAY),
            Component.text("Souls Stored: $count").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)
        ))
        item.itemMeta = meta
    }

    fun summonFlatMagicRings(player: Player, rings: Int) {
        val plugin = ProgressionPlus.getPlugin()
        val loc = player.location.clone()
        val world = loc.world

        val radiusStart = 3.0
        val radiusStep = 1.5
        val points = 100
        val durationTicks = 20L
        val rotateSpeed = 0.04

        for (ring in 0 until rings) {
            val radius = radiusStart + ring * radiusStep
            val rotationOffset = ring * Math.PI / 6

            object : BukkitRunnable() {
                var ticks = 0
                override fun run() {
                    if (ticks >= durationTicks) {
                        cancel()
                        return
                    }

                    for (i in 0 until points) {
                        val angle = 2 * Math.PI * i / points + ticks * rotateSpeed + rotationOffset
                        val x = loc.x + radius * Math.cos(angle)
                        val z = loc.z + radius * Math.sin(angle)
                        val y = loc.y + 0.05

                        val point = Location(world, x, y, z)

                        world.spawnParticle(Particle.WAX_OFF, point, 0)
                        world.spawnParticle(Particle.SCULK_SOUL, point, 0)
                        if (i % 10 == 0) {
                            world.spawnParticle(Particle.ENCHANTED_HIT, point.clone().add(0.0, 0.1, 0.0), 0)
                        }
                    }

                    ticks++
                }
            }.runTaskTimer(plugin, 0L, 2L)
        }
    }



}
