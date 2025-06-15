package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object EchoGun : CustomItem("echo_gun", Rarity.LEGENDARY) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.ECHO_SHARD)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Echo Gun")
                .color(NamedTextColor.DARK_AQUA)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Sonic Boom", Activation.RIGHT_CLICK),
                ItemLore.description("Charges for 1s then fires a deadly sonic wave in a straight line"),
                ItemLore.cooldown(30),
                ItemLore.separator(),
                ItemLore.lore("A remnant of ancient wrath, still echoing with vengeance."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "echo_gun")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun shootSonicBoom(player: Player) {
        val chargeTicks = 20L
        val world = player.world
        val location = player.location

        world.playSound(location, Sound.BLOCK_BEACON_POWER_SELECT, 1f, 1.5f)
        world.spawnParticle(Particle.END_ROD, player.eyeLocation, 30, 0.5, 0.5, 0.5, 0.01)
        player.sendActionBar(Component.text("⚡ Charging... ⚡").color(NamedTextColor.YELLOW))

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

            world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
        }, chargeTicks)
    }

    override fun getRecipe(): List<Material?> {
        return listOf(
            Material.ORANGE_WOOL, Material.ORANGE_WOOL, Material.ORANGE_WOOL,
            Material.ORANGE_WOOL, Material.GOLDEN_APPLE, Material.ORANGE_WOOL,
            null, Material.STICK, null
        )
    }
}