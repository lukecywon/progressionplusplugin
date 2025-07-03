package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Activation
import com.lukecywon.progressionPlus.utils.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.items.weapons.epic.EarthshatterHammer
import com.lukecywon.progressionPlus.utils.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object GravityMaul : CustomItem("gravity_maul", Rarity.LEGENDARY, enchantable = false) {

    private val jumpLockKey = NamespacedKey(ProgressionPlus.getPlugin(), "gravitymaul_jumplock")

    override fun createItemStack(): ItemStack {
        var item = ItemStack(Material.MACE)
        item = applyBaseDamage(item, 15.0)
        item = applyBaseAttackSpeed(item, 1.2)
        val meta = item.itemMeta!!

        meta.displayName(
            Component.text("Gravity Maul")
                .color(NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.BOLD, true)
        )

        meta.lore(
            listOf(
                ItemLore.abilityuse("Gravity Slam", Activation.RIGHT_CLICK),
                ItemLore.description("Pulls enemies inward then launches them sky-high"),
                ItemLore.cooldown(20),
                ItemLore.stats(item),
                ItemLore.separator(),
                ItemLore.lore("A cosmic weapon imbued with crushing force.")
            )
        )
        meta.itemModel = NamespacedKey(NamespacedKey.MINECRAFT, "gravity_maul")
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun slam(player: Player) {
        val world = player.world
        val origin = player.location
        val radius = 8.0

        world.playSound(origin, Sound.ENTITY_WITHER_BREAK_BLOCK, 1f, 0.6f)
        world.spawnParticle(Particle.PORTAL, origin, 80, 1.5, 1.5, 1.5, 0.1)

        val affected = world.getNearbyLivingEntities(origin, radius).filter { it != player }

        affected.forEach { entity ->
            // Apply Slowness and disable jump after delay
            Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
                affected.forEach { entity ->
                    if (entity is LivingEntity && !entity.isDead && entity.location.distance(origin) <= radius + 2) {
                        entity.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 70, 4, false, false, true))
                        entity.addPotionEffect(PotionEffect(PotionEffectType.DARKNESS, 70, 4, false, false, true))

                        val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)
                        if (jumpAttr != null) {
                            val data = entity.persistentDataContainer
                            if (!data.has(jumpLockKey, PersistentDataType.DOUBLE)) {
                                data.set(jumpLockKey, PersistentDataType.DOUBLE, jumpAttr.baseValue)
                                jumpAttr.baseValue = 0.0
                            }
                        }
                    }
                }
            }, 5L)

            world.spawnParticle(
                Particle.SMOKE,
                entity.location.clone().add(0.0, 1.0, 0.0),
                15, 0.3, 0.5, 0.3, 0.01
            )
            world.spawnParticle(
                Particle.DAMAGE_INDICATOR,
                entity.location.clone().add(0.0, 1.0, 0.0),
                10, 0.2, 0.4, 0.2, 0.02
            )

            val vector = origin.toVector().subtract(entity.location.toVector())
            val horizontal = vector.clone()
            horizontal.y = 0.0

            val distance = horizontal.length()
            if (distance > 0.5) {
                val scaledPull = horizontal.normalize().multiply(0.15 * distance.coerceAtMost(8.0))
                scaledPull.y = 0.35
                entity.velocity = scaledPull
            }
        }

        // Launch up (after 1 second)
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            affected.forEach { entity ->
                if (!entity.isDead && entity.location.distance(origin) <= radius) {
                    entity.velocity = entity.velocity.setY(1.5)
                    entity.world.spawnParticle(Particle.WITCH, entity.location, 30, 0.5, 1.0, 0.5, 0.1)
                }
            }

            player.velocity = player.velocity.setY(2.2)
            player.world.spawnParticle(Particle.CLOUD, player.location, 20, 1.0, 0.5, 1.0, 0.1)
            player.world.playSound(player.location, Sound.ITEM_TRIDENT_RIPTIDE_3, 1f, 1.5f)
        }, 20L)

        // Slam down (after 2.5 seconds)
        Bukkit.getScheduler().runTaskLater(ProgressionPlus.getPlugin(), Runnable {
            affected.forEach { entity ->
                if (!entity.isDead && entity.location.distance(origin) <= radius) {
                    entity.velocity = entity.velocity.setY(-2)
                    entity.world.playSound(entity.location, Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 0.7f)
                    entity.world.spawnParticle(Particle.EXPLOSION, entity.location, 10, 0.2, 0.2, 0.2, 0.1)
                }

                if (entity is LivingEntity) {
                    val data = entity.persistentDataContainer
                    if (data.has(jumpLockKey, PersistentDataType.DOUBLE)) {
                        val originalJump = data.get(jumpLockKey, PersistentDataType.DOUBLE) ?: return@forEach
                        val jumpAttr = entity.getAttribute(Attribute.JUMP_STRENGTH)
                        if (jumpAttr != null) {
                            jumpAttr.baseValue = originalJump
                        }
                        data.remove(jumpLockKey)
                    }
                }
            }

            player.velocity = player.velocity.setY(-0.5)
        }, 50L)

        // Sustained gravity pull (2.5 seconds, every 1 tick)
        var counter = 0
        Bukkit.getScheduler().runTaskTimer(ProgressionPlus.getPlugin(), Runnable {
            counter++
            if (counter > 50) return@Runnable

            affected.forEach { entity ->
                if (!entity.isDead && entity.location.distance(origin) <= radius + 2) {
                    val direction = origin.clone().subtract(entity.location).toVector()
                    direction.y = 0.0

                    val distance = direction.length()
                    if (distance > 0.5) {
                        val scaledPull = direction.normalize().multiply(0.015 * distance.coerceAtMost(6.0))
                        entity.velocity = entity.velocity.add(scaledPull)
                    }

                    entity.world.spawnParticle(Particle.CRIT, entity.location.clone().add(0.0, 1.0, 0.0), 2, 0.1, 0.2, 0.1, 0.01)
                }
            }
        }, 2L, 1L)
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.MaterialChoice(Material.MACE), RecipeChoice.ExactChoice(WardensHeart.createItemStack()),
            null, RecipeChoice.ExactChoice(EarthshatterHammer.createItemStack()), null,
            null, RecipeChoice.MaterialChoice(Material.END_ROD), null
        )
    }
}
