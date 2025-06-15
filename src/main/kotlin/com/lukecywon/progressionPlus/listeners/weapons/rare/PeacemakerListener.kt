package com.lukecywon.progressionPlus.listeners.weapons.rare

import com.lukecywon.progressionPlus.items.weapons.rare.Peacemaker
import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import net.kyori.adventure.text.Component
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.*

class PeacemakerListener : Listener {
    private val maxBullets = 6
    private val loadTimeTicks = 40L
    private val bulletKey = NamespacedKey(ProgressionPlus.getPlugin(), "bullets")
    private val loadTimers = mutableMapOf<UUID, Long>()
    private val activeReloadTasks = mutableMapOf<UUID, BukkitRunnable>()
    private val itemId = "peacemaker_shoot"
    private val cooldownMillis = 1000L
    private val pendingFanClicks = mutableSetOf<UUID>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (e.action == Action.RIGHT_CLICK_BLOCK &&
            item.type == Material.IRON_HOE &&
            item.itemMeta.persistentDataContainer.has(Peacemaker.key, PersistentDataType.BYTE)) {
            e.isCancelled = true
        }

        if (!item.itemMeta.persistentDataContainer.has(Peacemaker.key, PersistentDataType.BYTE)) return

        val meta = item.itemMeta
        val data = meta.persistentDataContainer
        val currentBullets = data.getOrDefault(bulletKey, PersistentDataType.INTEGER, 0)

        if (e.action == Action.LEFT_CLICK_AIR || e.action == Action.LEFT_CLICK_BLOCK) {
            if (currentBullets <= 0) return
            pendingFanClicks.add(player.uniqueId)
            fanTheHammer(player, currentBullets)
            data.set(bulletKey, PersistentDataType.INTEGER, 0)
            item.itemMeta = meta
            return
        }

        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        if (player.isSneaking) {
            if (loadTimers.containsKey(player.uniqueId)) {
                return
            }

            if (currentBullets >= maxBullets) {
                player.sendActionBar(buildBulletBar(currentBullets, null))
                player.sendActionBar(Component.text("§7[§fPeacemaker§7] §cChamber is full!"))
                return
            }

            val endTime = System.currentTimeMillis() + 10
            loadTimers[player.uniqueId] = endTime
            player.sendActionBar(Component.text("§7[§fPeacemaker§7] §fLoading bullet..."))

            val reloadTask = object : BukkitRunnable() {
                override fun run() {
                    val currentItem = player.inventory.itemInMainHand
                    if (!currentItem.itemMeta.persistentDataContainer.has(Peacemaker.key, PersistentDataType.BYTE)) {
                        loadTimers.remove(player.uniqueId)
                        activeReloadTasks.remove(player.uniqueId)
                        return
                    }

                    val updatedMeta = currentItem.itemMeta
                    val updatedData = updatedMeta.persistentDataContainer
                    val newCount = updatedData.getOrDefault(bulletKey, PersistentDataType.INTEGER, 0) + 1
                    updatedData.set(bulletKey, PersistentDataType.INTEGER, newCount)
                    currentItem.itemMeta = updatedMeta

                    player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_IRON, 1f, 1.3f)
                    animateBulletBar(player, newCount)
                    loadTimers.remove(player.uniqueId)
                    activeReloadTasks.remove(player.uniqueId)
                }
            }

            reloadTask.runTaskLater(ProgressionPlus.getPlugin(), loadTimeTicks)
            activeReloadTasks[player.uniqueId] = reloadTask

            object : BukkitRunnable() {
                override fun run() {
                    val now = System.currentTimeMillis()
                    val end = loadTimers[player.uniqueId] ?: run {
                        cancel()
                        return
                    }

                    val remaining = max(0, end - now)
                    val seconds = "%.1f".format(remaining / 1000.0)
                    if (remaining <= 0) {
                        cancel()
                        return
                    }

                    player.sendActionBar(buildBulletBar(currentBullets, seconds))
                }
            }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 4L)

            e.isCancelled = true
            return
        }

        if (currentBullets <= 0) {
            player.sendActionBar(buildBulletBar(0, null))
            player.sendActionBar(Component.text("§7[§fPeacemaker§7] §cNo bullets loaded."))
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 0.5f)
            return
        }

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            player.sendActionBar(Component.text("§7[§fPeacemaker§7] §cCooling down..."))
            return
        }
        // ❗ Cancel reload if mid-reload
        cancelReloadIfActive(player)

        fireLaser(player, 0.0)
        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis)

        val updatedMeta = item.itemMeta
        val updatedData = updatedMeta.persistentDataContainer
        updatedData.set(bulletKey, PersistentDataType.INTEGER, currentBullets - 1)
        item.itemMeta = updatedMeta

        animateBulletBar(player, currentBullets - 1)

        val loc = player.location
        val pitch = 1.2f + (Math.random() * 0.2f).toFloat()
        player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, pitch)
        player.playSound(loc, Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.6f, 2.0f)
        player.playSound(loc, Sound.BLOCK_DISPENSER_LAUNCH, 0.4f, 1.4f)

        e.isCancelled = true
    }

    private fun fanTheHammer(player: Player, bulletCount: Int) {
        val maxSpread = 25.0
        val exponent = 1.6
        val base = (bulletCount - 1).toDouble().coerceAtLeast(0.0) / 5.0
        val actualSpread = min(maxSpread, maxSpread * base.pow(exponent))
        val step = if (bulletCount == 1) 0.0 else actualSpread / (bulletCount - 1)
        val startAngle = -actualSpread / 2

        object : BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i >= bulletCount) {
                    cancel()
                    return
                }

                val angle = startAngle + i * step
                val recoil = 1.0 + (i * 0.4)
                fireLaser(player, angle, recoil)

                val bulletsLeft = bulletCount - i - 1
                animateBulletBar(player, bulletsLeft)

                player.playSound(player.location, Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1.05f + i * 0.01f)
                player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.5f, 2.0f)
                i++
            }
        }.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 2L)
    }

    private fun fireLaser(player: Player, yawOffsetDegrees: Double, recoilAmount: Double = 1.0) {
        val eye = player.eyeLocation.clone()
        val dir = eye.direction.clone()

        if (yawOffsetDegrees != 0.0) {
            val yaw = eye.yaw + yawOffsetDegrees.toFloat()
            val pitch = eye.pitch
            val adjustedLoc = eye.clone()
            adjustedLoc.yaw = yaw
            adjustedLoc.pitch = pitch
            dir.copy(adjustedLoc.direction)
        }

        dir.add(Vector(
            (Math.random() - 0.5) * 0.008 * recoilAmount,
            (Math.random() - 0.5) * 0.006 * recoilAmount,
            (Math.random() - 0.5) * 0.008 * recoilAmount
        )).normalize()

        val step = dir.clone().multiply(0.5)
        var current = eye.clone()

        for (i in 0..100) {
            current.add(step)
            player.world.spawnParticle(Particle.DUST, current, 1, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.WHITE, 1.0f))
            if (current.block.type.isSolid) break

            val target = player.world.getNearbyEntities(current, 0.3, 0.3, 0.3)
                .firstOrNull { it is LivingEntity && it != player } as? LivingEntity

            if (target != null) {
                target.noDamageTicks = 0
                target.damage(6.0, player)
                player.world.spawnParticle(Particle.DAMAGE_INDICATOR, target.location.add(0.0, 1.0, 0.0), 5)
                break
            }
        }
    }

    private fun animateBulletBar(player: Player, bullets: Int) {
        player.sendActionBar(buildBulletBar(bullets, null))
    }

    private fun buildBulletBar(bullets: Int, timeText: String?): Component {
        val green = "▌".repeat(bullets)
        val red = "▌".repeat(maxBullets - bullets)
        val timer = timeText?.let { " §e${it}s" } ?: ""
        return Component.text("§fBullets: §a$green§c$red$timer")
    }

    @EventHandler
    fun onItemHeld(e: PlayerItemHeldEvent) {
        cancelReloadIfActive(e.player)
    }

    @EventHandler
    fun onItemDrop(e: PlayerDropItemEvent) {
        cancelReloadIfActive(e.player)
    }

    private fun cancelReloadIfActive(player: Player) {
        val task = activeReloadTasks.remove(player.uniqueId)
        if (task != null) {
            task.cancel()
            loadTimers.remove(player.uniqueId)
            player.sendActionBar(Component.text("§7[§fPeacemaker§7] §cReload cancelled."))
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 0.7f)
        }
    }

    @EventHandler
    fun onEntityHit(e: EntityDamageByEntityEvent) {
        val player = e.damager as? Player ?: return
        val item = player.inventory.itemInMainHand

        if (!item.itemMeta.persistentDataContainer.has(Peacemaker.key, PersistentDataType.BYTE)) return
        if (!pendingFanClicks.remove(player.uniqueId)) return

        e.isCancelled = true
    }
}
