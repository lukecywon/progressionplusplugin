package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.items.weapons.rare.ResonantBlade
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

object VoidReaper : CustomItem("void_reaper", Rarity.LEGENDARY) {
    private val soulKey = NamespacedKey("survivaltestplugin", "souls")

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.NETHERITE_HOE)

        item = applyBaseDamage(item, 11.0)
        item = applyBaseAttackSpeed(item, 4.0)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Void Reaper")
                .color(NamedTextColor.DARK_PURPLE)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Void Slash", Activation.LEFT_CLICK),
                ItemLore.description("Teleport behind and slash the enemy you're looking at"),
                ItemLore.cooldown(7),
                ItemLore.abilityuse("Soulburst", Activation.RIGHT_CLICK),
                ItemLore.description("Unleash stored souls in an AOE blast"),
                Component.text("Souls Stored: 0").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false),
                ItemLore.cooldown(7),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Forged in the void, it whispers with the cries of the condemned."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "void_reaper")
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
        if (current >= 30) return
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

        world.playSound(loc, Sound.ENTITY_WITHER_SPAWN, 1f, 0.8f)
        world.playSound(loc, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1.2f)
        world.playSound(loc, Sound.ITEM_TRIDENT_THUNDER, 1f, 1.2f)
        world.spawnParticle(Particle.EXPLOSION, loc, 1)

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

        for (layer in 0..6) {
            val spiralY = y + layer * 0.25
            for (i in 0 until 20) {
                val angle = Math.PI * 2 * i / 20 + layer * 0.5
                val spiralX = loc.x + Math.cos(angle) * (circleradius - layer * 0.4)
                val spiralZ = loc.z + Math.sin(angle) * (circleradius - layer * 0.4)
                world.spawnParticle(Particle.SCULK_SOUL, Location(world, spiralX, spiralY, spiralZ), 1, 0.0, 0.0, 0.0, 0.01)
            }
        }

        val (ringCount, radius) = when {
            souls >= 3 -> 3 to 6.0
            souls >= 2 -> 2 to 4.5
            else        -> 1 to 3.0
        }

        summonFlatMagicRings(player, ringCount)

        world.playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1.2f)
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(0.0, 0.2, 0.0), 40, 0.5, 0.0, 0.5, 0.01)
        world.spawnParticle(Particle.DRAGON_BREATH, loc.clone().add(0.0, 0.4, 0.0), 20, 0.3, 0.0, 0.3, 0.01)

        world.getNearbyEntities(loc, radius, radius, radius).forEach {
            if (it is LivingEntity && it != player) {
                it.damage(5.0 * souls, player)
                it.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 1))
            }
        }

        player.setCooldown(Material.NETHERITE_HOE, 60)
        player.sendMessage("§5Void Ritual unleashed! §d$souls §5souls consumed.")
        player.sendActionBar("§b☠ Soulburst Casted!")
    }

    fun updateLore(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val count = getSoulCount(item)
        meta.lore(
            listOf(
                ItemLore.abilityuse("Void Slash", Activation.LEFT_CLICK),
                ItemLore.description("Teleport behind and slash the enemy you're looking at"),
                ItemLore.abilityuse("Soulburst", Activation.RIGHT_CLICK),
                ItemLore.description("Unleash stored souls in an AOE blast"),
                Component.text("Souls Stored: $count").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false),
                ItemLore.cooldown(2),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("Forged in the void, it whispers with the cries of the condemned."),
            )
        )
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

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.ExactChoice(EchoCore.createItemStack()),
            null, RecipeChoice.ExactChoice(WardensHeart.createItemStack()), null,
            RecipeChoice.MaterialChoice(Material.END_ROD), null, null
        )
    }
}
