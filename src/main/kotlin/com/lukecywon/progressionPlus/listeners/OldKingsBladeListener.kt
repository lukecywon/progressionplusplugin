package com.lukecywon.progressionPlus.listeners

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.OldKingsBlade
import com.lukecywon.progressionPlus.items.TwilightCrown
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class OldKingsBladeListener : Listener {
    val itemId = "old_kings_blade"
    private val plugin = ProgressionPlus.getPlugin()
    private val SUMMON_KEY = NamespacedKey(plugin, "summoner_uuid")
    private val activeSummons = mutableMapOf<UUID, List<LivingEntity>>()

    @EventHandler
    fun onRightClick(e: PlayerInteractEvent) {
        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!OldKingsBlade.isOldKingsBlade(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        val hasCrown = TwilightCrown.isTwilightCrown(player.inventory.helmet)
        val cooldownMillis = if (hasCrown) 1.5 * 60 * 1000 else 3 * 60 * 1000

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cYou must wait ${minutes}m ${seconds}s before summoning again.")
            return
        }

        if (player.isSneaking) {
            activeSummons[player.uniqueId]?.forEach { if (!it.isDead) it.remove() }
            activeSummons.remove(player.uniqueId)
            player.sendMessage("§7Your undead followers have been dismissed.")
            return
        }

        activeSummons[player.uniqueId]?.forEach { if (!it.isDead) it.remove() }

        val world = player.world
        val origin = player.location
        val summoned = mutableListOf<LivingEntity>()

        // --- Zombies ---
        repeat(3) {
            val loc = origin.clone().add((Math.random() - 0.5) * 4, 0.0, (Math.random() - 0.5) * 4)
            val zombie = world.spawn(loc, Zombie::class.java)
            zombie.customName = if (hasCrown) "§5Undead Guard" else "§7Wandering Thrall"
            zombie.isCustomNameVisible = true
            zombie.setAdult()
            zombie.isPersistent = false
            zombie.setRemoveWhenFarAway(false)
            zombie.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, false, false))
            if (hasCrown) zombie.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 999999, 1, false, false))

            tagSummon(zombie, player)
            equipMob(zombie,
                weapon = ItemStack(if (hasCrown) Material.IRON_SWORD else Material.STONE_SWORD),
                isBowUser = false,
                useDiamondArmor = false,
                useLeather = !hasCrown
            )
            startTargetingTask(zombie, player)
            summoned.add(zombie)
        }

        // --- Skeletons ---
        repeat(if (hasCrown) 3 else 1) { i ->
            val loc = origin.clone().add((Math.random() - 0.5) * 4, 0.0, (Math.random() - 0.5) * 4)
            val skeleton = world.spawn(loc, Skeleton::class.java)
            val isKnight = hasCrown && i == 0

            skeleton.customName = when {
                isKnight -> "§9Twilight Knight"
                hasCrown -> "§5Twilight Archer"
                else -> "§7Restless Bowman"
            }

            skeleton.isCustomNameVisible = true
            skeleton.isPersistent = false
            skeleton.setRemoveWhenFarAway(false)
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 999999, 0, false, false))

            tagSummon(skeleton, player)

            if (isKnight) {
                val horse = world.spawn(loc, SkeletonHorse::class.java)
                horse.isTamed = true
                horse.isPersistent = false
                horse.setRemoveWhenFarAway(false)
                horse.addPassenger(skeleton)
                tagSummon(horse, player)
                summoned.add(horse)
                val bow = ItemStack(Material.BOW)
                bow.addUnsafeEnchantment(Enchantment.POWER, 5)
                bow.addUnsafeEnchantment(Enchantment.FLAME, 2)
                equipMob(skeleton, bow, isBowUser = true, useDiamondArmor = true, protectionLevel = 4)

            } else {
                equipMob(skeleton, ItemStack(Material.BOW), true, false, !hasCrown)
            }

            startTargetingTask(skeleton, player)
            summoned.add(skeleton)
        }

        // --- Phantoms (only with crown) ---
        if (hasCrown) {
            repeat(3) {
                val loc = origin.clone().add((Math.random() - 0.5) * 6, 8.0, (Math.random() - 0.5) * 6)
                val phantom = world.spawn(loc, Phantom::class.java)
                phantom.customName = "§5Void Wraith"
                phantom.isCustomNameVisible = true
                phantom.isPersistent = false
                phantom.setRemoveWhenFarAway(false)
                phantom.setSilent(true)
                phantom.setShouldBurnInDay(false)
                tagSummon(phantom, player)
                startTargetingTask(phantom, player, range = 30.0, height = 20.0)
                summoned.add(phantom)
            }
        }

        activeSummons[player.uniqueId] = summoned
        CustomItem.setCooldown(itemId, player.uniqueId, cooldownMillis.toLong())

        world.playSound(origin, Sound.ENTITY_WITHER_SPAWN, 1f, 0.6f)
        world.spawnParticle(Particle.SOUL_FIRE_FLAME, origin, 30, 1.5, 1.0, 1.5, 0.1)

        if (hasCrown) {
            player.sendMessage("§5The monarch has returned. The army awakens.")
        } else {
            player.sendMessage("§7The nearby deceased rise at your command.")
        }
    }

    @EventHandler
    fun onTarget(e: EntityTargetLivingEntityEvent) {
        val entity = e.entity as? Monster ?: return
        val target = e.target ?: return
        val summonerId = entity.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING)
        if (summonerId != null && target.uniqueId.toString() == summonerId) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onSummonedMobDeath(e: EntityDeathEvent) {
        val entity = e.entity
        val summonerId = entity.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING)
        if (summonerId != null) {
            e.drops.clear()
            e.droppedExp = 0
        }
    }

    private fun tagSummon(entity: LivingEntity, summoner: Player) {
        entity.persistentDataContainer.set(SUMMON_KEY, PersistentDataType.STRING, summoner.uniqueId.toString())
    }

    private fun startTargetingTask(mob: Mob, summoner: Player, range: Double = 12.0, height: Double = 6.0) {
        object : BukkitRunnable() {
            override fun run() {
                if (!mob.isValid || mob.isDead) {
                    cancel()
                    return
                }

                val targets = mob.getNearbyEntities(range, height, range)
                    .filterIsInstance<LivingEntity>()
                    .filter {
                        it != summoner &&
                                it !is ArmorStand &&
                                it !is Item &&
                                it !is Bat &&
                                it.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING) != summoner.uniqueId.toString() &&
                                !(it is Tameable && it.isTamed && it.owner?.uniqueId == summoner.uniqueId)
                    }

                mob.target = targets.randomOrNull()
            }
        }.runTaskTimer(plugin, 40L, 40L)
    }

    private fun equipMob(
        entity: LivingEntity,
        weapon: ItemStack,
        isBowUser: Boolean,
        useDiamondArmor: Boolean = false,
        useLeather: Boolean = false,
        protectionLevel: Int = 2 // Default to Prot II
    ) {
        val material = when {
            useDiamondArmor -> "DIAMOND"
            useLeather -> "LEATHER"
            else -> "IRON"
        }

        val armor = listOf(
            ItemStack(Material.valueOf("${material}_HELMET")),
            ItemStack(Material.valueOf("${material}_CHESTPLATE")),
            ItemStack(Material.valueOf("${material}_LEGGINGS")),
            ItemStack(Material.valueOf("${material}_BOOTS"))
        )

        armor.forEach {
            it.addUnsafeEnchantment(Enchantment.PROTECTION, protectionLevel)
            it.itemMeta = it.itemMeta.apply {
                isUnbreakable = true
                addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
            }
        }

        weapon.itemMeta = weapon.itemMeta.apply {
            isUnbreakable = true
            addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
        }

        val eq = entity.equipment!!
        eq.helmet = armor[0]
        eq.chestplate = armor[1]
        eq.leggings = armor[2]
        eq.boots = armor[3]
        eq.setItemInMainHand(weapon)

        eq.helmetDropChance = 0f
        eq.chestplateDropChance = 0f
        eq.leggingsDropChance = 0f
        eq.bootsDropChance = 0f
        eq.itemInMainHandDropChance = 0f
    }

}
