package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object SacrificialClock : CustomItem("sacrificial_clock", Rarity.LEGENDARY) {
    private val contributionScores = mapOf(
        Material.GOLD_NUGGET to 1,
        Material.GOLD_INGOT to 10,
        Material.GOLDEN_CARROT to 10,
        Material.GOLDEN_APPLE to 100,
        Material.GOLD_BLOCK to 100,
        Material.GOLDEN_HELMET to 10,
        Material.GOLDEN_CHESTPLATE to 10,
        Material.GOLDEN_LEGGINGS to 10,
        Material.GOLDEN_BOOTS to 10,
        Material.GOLDEN_SWORD to 10,
        Material.GOLDEN_AXE to 10,
        Material.GOLDEN_PICKAXE to 10,
        Material.GOLDEN_SHOVEL to 10,
        Material.GOLDEN_HOE to 10
    )

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.CLOCK)
        val meta = item.itemMeta

        meta.displayName(Component.text("Sacrificial Clock").color(NamedTextColor.GOLD))
        meta.lore(listOf(
            Component.text("Right-click to offer golden items for blessings.").color(NamedTextColor.GRAY),
            Component.text("One legendary item allowed at a time.").color(NamedTextColor.DARK_RED)
        ))
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isSacrificialClock(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.CLOCK) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    fun getScoreFromItem(item: ItemStack): Int {
        return contributionScores[item.type] ?: 0
    }

    fun applyTierBuffs(player: Player, score: Int) {
        when {
            score >= 5000 -> {
                applyAllBuffs(player, 180, 300, 60)
                playTierParticles(player, 5)
            }
            score >= 2500 -> {
                applyTierIVBuffs(player)
                playTierParticles(player, 4)
            }
            score >= 1000 -> {
                applyTierIIIBuffs(player)
                playTierParticles(player, 3)
            }
            score >= 300 -> {
                applyTierIIBuffs(player)
                playTierParticles(player, 2)
            }
            score >= 100 -> {
                applyTierIBuffs(player)
                playTierParticles(player, 1)
            }
            else -> player.sendMessage("§cYour offering was too small to gain blessings.")
        }
    }

    private fun applyTierIBuffs(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 60 * 3 * 20, 1))
    }

    private fun applyTierIIBuffs(player: Player) {
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 60 * 4 * 20, 2))
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 10 * 20, 1))
        object : BukkitRunnable() {
            override fun run() {
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 50 * 20, 0))
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 10 * 20L)
    }

    private fun applyTierIIIBuffs(player: Player) {
        player.health = player.getAttribute(Attribute.MAX_HEALTH)?.value ?: 20.0
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 60 * 4 * 20, 3))
        player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 30 * 20, 1))
        object : BukkitRunnable() {
            override fun run() {
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 120 * 20, 0))
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 30 * 20L)
        player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 120 * 20, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.WATER_BREATHING, 120 * 20, 0))
        player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 120 * 20, 0))
    }

    private fun applyTierIVBuffs(player: Player) {
        applyTierIIIBuffs(player)
        player.addPotionEffect(PotionEffect(PotionEffectType.HASTE, 120 * 20, 1))
        player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 30 * 20, 1))
        object : BukkitRunnable() {
            override fun run() {
                player.addPotionEffect(PotionEffect(PotionEffectType.STRENGTH, 90 * 20, 0))
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 30 * 20L)

        val attribute = player.getAttribute(Attribute.MAX_HEALTH) ?: return
        val originalHealth = attribute.baseValue
        attribute.baseValue = originalHealth + 10.0

        object : BukkitRunnable() {
            override fun run() {
                attribute.baseValue = attribute.baseValue - 10.0
                if (player.health > attribute.baseValue) player.health = attribute.baseValue
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), 120 * 20L)

        player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 120 * 20, 0))
    }

    private fun applyAllBuffs(player: Player, normalDuration: Int, absorptionDuration: Int, resistanceDuration: Int) {
        applyTierIVBuffs(player)
        player.addPotionEffect(PotionEffect(PotionEffectType.RESISTANCE, resistanceDuration * 20, 4))
        player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, absorptionDuration * 20, 4))
    }

    private fun playTierParticles(player: Player, tier: Int) {
        val world = player.world
        val loc = player.location.clone().add(0.0, 1.0, 0.0)

        val (type, count) = when (tier) {
            1 -> Particle.CRIT to 20
            2 -> Particle.FLASH to 30
            3 -> Particle.HAPPY_VILLAGER to 40
            4 -> Particle.TOTEM_OF_UNDYING to 1
            5 -> Particle.SOUL_FIRE_FLAME to 100
            else -> Particle.SMOKE to 10
        }

        world.spawnParticle(type, loc, count, 0.5, 1.0, 0.5)
    }
}
