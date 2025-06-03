package com.ervinyap.survivalTestPlugin.items

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
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
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

object VoidReaper : CustomItem("void_reaper", Rarity.LEGENDARY) {
    private val soulKey = NamespacedKey("survivaltestplugin", "souls")

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
            Component.text("Left-click: Teleport and slash behind enemies you are looking at").color(NamedTextColor.DARK_GRAY),
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

        if (souls <= 0) {
            player.sendMessage("§7No souls to unleash.")
            return
        }

        world.spawnParticle(Particle.DRAGON_BREATH, player.location, 100, 2.0, 1.0, 2.0, 0.1)
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, player.location, 50, 1.0, 1.0, 1.0, 0.05)
        world.spawnParticle(Particle.EXPLOSION, player.location, 1)
        world.playSound(player.location, Sound.ENTITY_WITHER_SPAWN, 1f, 0.5f)

        val radius = 5.0
        world.getNearbyEntities(player.location, radius, radius, radius).forEach {
            if (it is LivingEntity && it != player) {
                it.damage(4.0 * souls, player)
                it.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 40, 1))
            }
        }

        player.setCooldown(Material.NETHERITE_HOE, 60)
        player.sendMessage("§5Unleashed $souls soul(s) in a burst!")
    }

    fun slashTeleport(player: Player) {
        val target = player.getTargetEntity(10) as? LivingEntity
        if (target == null || target == player) {
            player.sendMessage("§7No valid target in sight.")
            player.world.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f)
            return
        }

        val behind = target.location.clone().add(target.location.direction.multiply(-1)).apply {
            y = target.location.y
        }

        player.teleport(behind)
        player.world.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.7f)
        player.world.playSound(player.location, Sound.ENTITY_WARDEN_HEARTBEAT, 1f, 1f)

        target.damage(8.0, player)
        target.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 60, 1))
        target.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 60, 1))

        player.spawnParticle(Particle.PORTAL, player.eyeLocation, 30, 0.5, 0.5, 0.5, 0.1)
        player.spawnParticle(Particle.SWEEP_ATTACK, target.location.add(0.0, 1.0, 0.0), 5)
    }

    fun updateLore(item: ItemStack) {
        val meta = item.itemMeta ?: return
        val count = getSoulCount(item)
        meta.lore(listOf(
            Component.text("§6LEGENDARY"),
            Component.text("Right-click: Unleash stored souls in an AOE blast").color(NamedTextColor.GRAY),
            Component.text("Left-click: Teleport and slash behind enemies you are looking at").color(NamedTextColor.DARK_GRAY),
            Component.text("Souls Stored: $count").color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false)
        ))
        item.itemMeta = meta
    }
}
