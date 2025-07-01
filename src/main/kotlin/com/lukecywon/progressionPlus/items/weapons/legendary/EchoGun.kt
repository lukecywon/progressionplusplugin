package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.EchoCore
import com.lukecywon.progressionPlus.items.component.EnderiteIngot
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

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
                ItemLore.description("doing 30 damage."),
                ItemLore.cooldown(20),
                ItemLore.separator(),
                ItemLore.lore("A remnant of ancient wrath, still echoing with vengeance."),
            )
        )

        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "echo_gun")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun shootSonicBoom(player: Player) {
        val chargeTicks = 30L
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
                world.spawnParticle(Particle.SONIC_BOOM, point, 3, 0.0, 0.0, 0.0, 0.0)

                val nearby = world.getNearbyEntities(point, 1.0, 1.0, 1.0)
                for (entity in nearby) {
                    if (entity != player && entity is LivingEntity) {
                        entity.damage(30.0, player)

                        if (entity is Player) {
                            // Check if player is holding a shield and blocking
                            if ((entity.isBlocking && entity.inventory.itemInOffHand.type == Material.SHIELD) || (entity.isBlocking && entity.inventory.itemInMainHand.type == Material.SHIELD)) {
                                entity.setCooldown(Material.SHIELD, 100) // 100 ticks = 5 seconds
                                entity.world.playSound(player.location, Sound.ITEM_SHIELD_BREAK, 1f, 1f)
                            }

                            // Remove random potion effect
                            removeRandomPotionEffect(entity)

                            // Add nausea
                            entity.addPotionEffect(PotionEffect(
                                PotionEffectType.NAUSEA, 2, 2, false, false
                            ))
                        }

                        world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
                        world.playSound(point, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
                    }
                }
            }

            world.playSound(player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, 1f)
        }, chargeTicks)
    }

    private fun removeRandomPotionEffect(player: Player) {
        val activeEffects = player.activePotionEffects.toList()

        if (activeEffects.isNotEmpty()) {
            val randomEffect = activeEffects.random()
            player.removePotionEffect(randomEffect.type)
            player.sendMessage("§cA mysterious force has removed your ${randomEffect.type.name.lowercase().replace('_', ' ')} effect.")
        }
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT), RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT), RecipeChoice.MaterialChoice(Material.DIAMOND),
            RecipeChoice.ExactChoice(EnderiteIngot.createItemStack()), RecipeChoice.ExactChoice(EchoCore.createItemStack()), RecipeChoice.ExactChoice(WardensHeart.createItemStack()),
            null, null, RecipeChoice.MaterialChoice(Material.STICK)
        )
    }
}