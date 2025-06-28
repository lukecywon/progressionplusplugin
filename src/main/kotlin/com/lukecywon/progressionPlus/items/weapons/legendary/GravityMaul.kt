package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Activation
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.component.WardensHeart
import com.lukecywon.progressionPlus.items.weapons.epic.EarthshatterHammer
import com.lukecywon.progressionPlus.mechanics.ItemLore
import com.lukecywon.progressionPlus.recipes.RecipeGenerator
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.persistence.PersistentDataType

object GravityMaul : CustomItem("gravity_maul", Rarity.LEGENDARY) {

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

            val vector = origin.toVector().subtract(entity.location.toVector()).normalize().multiply(1.5)
            vector.y = 0.4
            entity.velocity = vector
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
                    entity.velocity = entity.velocity.setY(-1.8)
                    entity.world.playSound(entity.location, Sound.ENTITY_IRON_GOLEM_ATTACK, 1f, 0.7f)
                    entity.world.spawnParticle(Particle.EXPLOSION, entity.location, 10, 0.2, 0.2, 0.2, 0.1)
                }
            }

            player.velocity = player.velocity.setY(-2.5)
        }, 50L)

        // Sustained gravity pull (for 2.5 seconds, every 5 ticks)
        var counter = 0
        Bukkit.getScheduler().runTaskTimer(ProgressionPlus.getPlugin(), Runnable {
            counter++
            if (counter > 10) return@Runnable

            affected.forEach { entity ->
                if (!entity.isDead && entity.location.distance(origin) <= radius + 2) {
                    val pull = origin.toVector().subtract(entity.location.toVector()).normalize().multiply(0.3)
                    pull.y = 0.05
                    entity.velocity = entity.velocity.add(pull)

                    // Optional: add subtle particles
                    entity.world.spawnParticle(Particle.CRIT, entity.location.clone().add(0.0, 1.0, 0.0), 3, 0.1, 0.2, 0.1, 0.02)
                }
            }
        }, 5L, 5L) // delay, interval
    }

    override fun getRecipe(): List<RecipeChoice?> {
        return listOf(
            RecipeChoice.ExactChoice(WardensHeart.createItemStack()), RecipeChoice.MaterialChoice(Material.MACE), RecipeChoice.ExactChoice(WardensHeart.createItemStack()),
            null, RecipeChoice.ExactChoice(EarthshatterHammer.createItemStack()), null,
            null, RecipeChoice.MaterialChoice(Material.END_ROD), null
        )
    }
}
