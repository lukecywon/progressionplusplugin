package com.lukecywon.progressionPlus.listeners.weapons.rare

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.items.weapons.rare.OldKingsBlade
import com.lukecywon.progressionPlus.items.armor.legendary.TwilightCrown
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
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
        if (e.hand != EquipmentSlot.HAND) return

        val player = e.player
        val item = player.inventory.itemInMainHand

        if (!OldKingsBlade.isOldKingsBlade(item)) return
        if (e.action != Action.RIGHT_CLICK_AIR && e.action != Action.RIGHT_CLICK_BLOCK) return

        e.isCancelled = true

        if (player.isSneaking) {
            activeSummons[player.uniqueId]?.forEach { if (!it.isDead) it.remove() }
            activeSummons.remove(player.uniqueId)
            player.sendMessage("§7Your undead followers have been dismissed.")
            return
        }

        val hasCrown = TwilightCrown.isThisItem(player.inventory.helmet)
        val cooldownMillis = if (hasCrown) 90_000L else 180_000L

        if (CustomItem.isOnCooldown(itemId, player.uniqueId)) {
            val millisLeft = CustomItem.getCooldownRemaining(itemId, player.uniqueId)
            val minutes = (millisLeft / 1000) / 60
            val seconds = (millisLeft / 1000) % 60
            player.sendMessage("§cYou must wait ${minutes}m ${seconds}s before summoning again.")
            return
        }

        activeSummons[player.uniqueId]?.forEach { if (!it.isDead) it.remove() }

        val world = player.world
        val origin = player.location
        val summoned = mutableListOf<LivingEntity>()

        // Zombies
        val zombieCount = if (hasCrown) 4 else 3
        for (i in 0 until zombieCount) {
            val loc = origin.clone().add((Math.random() - 0.5) * 4, 0.0, (Math.random() - 0.5) * 4)
            val zombie = world.spawn(loc, Zombie::class.java)
            val isGeneral = hasCrown && i == 0
            val name = when {
                isGeneral -> "§9${player.name}'s Undead General"
                hasCrown -> "§5${player.name}'s Undead Guard"
                else -> "§7${player.name}'s Wandering Thrall"
            }
            zombie.customName = name
            zombie.isCustomNameVisible = true
            zombie.setAdult()
            zombie.isPersistent = false
            zombie.setRemoveWhenFarAway(false)
            zombie.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0, false, false))

            if (hasCrown) {
                val speedLevel = if (isGeneral) 2 else 1
                zombie.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, speedLevel, false, false))
            }

            val weapon = ItemStack(if (isGeneral) Material.DIAMOND_SWORD else if (hasCrown) Material.IRON_SWORD else Material.STONE_SWORD)
            if (isGeneral) weapon.addUnsafeEnchantment(Enchantment.SHARPNESS, 4)
            else if (hasCrown) weapon.addUnsafeEnchantment(Enchantment.SHARPNESS, 2)
            else weapon.addUnsafeEnchantment(Enchantment.SHARPNESS, 1)

            tagSummon(zombie, player)
            equipMob(zombie, weapon, false,
                useDiamondArmor = isGeneral,
                useLeather = !hasCrown,
                protectionLevel = if (isGeneral) 3 else if (hasCrown) 2 else 1
            )
            startTargetingTask(zombie, player)
            summoned.add(zombie)
        }

        // Skeletons
        val skeletonCount = if (hasCrown) 3 else 1
        for (i in 0 until skeletonCount) {
            val loc = origin.clone().add((Math.random() - 0.5) * 4, 0.0, (Math.random() - 0.5) * 4)
            val skeleton = world.spawn(loc, Skeleton::class.java)
            val isKnight = hasCrown && i == 0

            val name = when {
                isKnight -> "§9${player.name}'s Twilight Knight"
                hasCrown -> "§5${player.name}'s Twilight Archer"
                else -> "§7${player.name}'s Restless Bowman"
            }
            skeleton.customName = name
            skeleton.isCustomNameVisible = true
            skeleton.isPersistent = false
            skeleton.setRemoveWhenFarAway(false)
            skeleton.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0, false, false))

            if (hasCrown) skeleton.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 0, false, false))

            tagSummon(skeleton, player)

            val bow = ItemStack(Material.BOW)
            bow.addUnsafeEnchantment(Enchantment.POWER, if (isKnight) 5 else if (hasCrown) 3 else 1)
            if (isKnight) bow.addUnsafeEnchantment(Enchantment.FLAME, 2)

            equipMob(skeleton, bow, true,
                useDiamondArmor = isKnight,
                useLeather = false,
                useChainmail = !hasCrown,
                protectionLevel = if (isKnight) 4 else 2
            )

            if (isKnight) {
                val horse = world.spawn(loc, SkeletonHorse::class.java)
                horse.isTamed = true
                horse.isPersistent = false
                horse.setRemoveWhenFarAway(false)
                horse.addPotionEffect(PotionEffect(PotionEffectType.SPEED, Int.MAX_VALUE, 0, false, false))
                horse.addPassenger(skeleton)
                tagSummon(horse, player)
                summoned.add(horse)
            }

            startTargetingTask(skeleton, player)
            summoned.add(skeleton)
        }

        // Phantoms
        if (hasCrown) {
            repeat(3) {
                val loc = origin.clone().add((Math.random() - 0.5) * 6, 8.0, (Math.random() - 0.5) * 6)
                val phantom = world.spawn(loc, Phantom::class.java)
                phantom.customName = "§5${player.name}'s Void Wraith"
                phantom.isCustomNameVisible = true
                phantom.isPersistent = false
                phantom.setRemoveWhenFarAway(false)
                phantom.setSilent(true)
                phantom.setShouldBurnInDay(false)
                tagSummon(phantom, player)
                startTargetingTask(phantom, player, 40.0, 30.0)
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

    private fun startTargetingTask(mob: Mob, summoner: Player, range: Double = 20.0, height: Double = 10.0) {
        object : BukkitRunnable() {
            override fun run() {
                if (!mob.isValid || mob.isDead) {
                    cancel()
                    return
                }
                val targets = mob.getNearbyEntities(range, height, range)
                    .filterIsInstance<LivingEntity>()
                    .filter {
                        it != summoner && it !is ArmorStand && it !is Item && it !is Bat &&
                                it.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING) != summoner.uniqueId.toString() &&
                                !(it is Tameable && it.isTamed && it.owner?.uniqueId == summoner.uniqueId)
                    }
                mob.target = targets.randomOrNull()
            }
        }.runTaskTimer(plugin, 10L, 40L)
    }

    private fun equipMob(
        entity: LivingEntity,
        weapon: ItemStack,
        isBowUser: Boolean,
        useDiamondArmor: Boolean = false,
        useLeather: Boolean = false,
        useChainmail: Boolean = false,
        protectionLevel: Int = 2
    ){
        val armorMaterial = when {
            useDiamondArmor -> "DIAMOND"
            useLeather -> "LEATHER"
            useChainmail -> "CHAINMAIL"
            else -> "IRON"
        }

        val armor = listOf(
            Material.valueOf("${armorMaterial}_HELMET"),
            Material.valueOf("${armorMaterial}_CHESTPLATE"),
            Material.valueOf("${armorMaterial}_LEGGINGS"),
            Material.valueOf("${armorMaterial}_BOOTS")
        ).map {
            ItemStack(it).apply {
                addUnsafeEnchantment(Enchantment.PROTECTION, protectionLevel)
                itemMeta = itemMeta.apply {
                    isUnbreakable = true
                    addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
                }
            }
        }

        weapon.itemMeta = weapon.itemMeta?.apply {
            isUnbreakable = true
            addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
        }

        entity.equipment?.apply {
            helmet = armor[0]
            chestplate = armor[1]
            leggings = armor[2]
            boots = armor[3]
            setItemInMainHand(weapon)
            helmetDropChance = 0f
            chestplateDropChance = 0f
            leggingsDropChance = 0f
            bootsDropChance = 0f
            itemInMainHandDropChance = 0f
        }
    }

    @EventHandler
    fun onSummonedProjectileHit(e: EntityDamageByEntityEvent) {
        val damager = e.damager
        val target = e.entity as? LivingEntity ?: return

        // Only check for projectile damage from skeletons
        if (damager is Projectile) {
            val shooter = damager.shooter as? LivingEntity ?: return

            val shooterSummonerId = shooter.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING)
            if (shooterSummonerId != null) {
                val summonerUUID = UUID.fromString(shooterSummonerId)

                // Cancel if the projectile hits the summoner
                if (target.uniqueId == summonerUUID) {
                    e.isCancelled = true
                    return
                }

                // Cancel if the projectile hits other summons or tamed mobs
                if (target.persistentDataContainer.get(SUMMON_KEY, PersistentDataType.STRING) == shooterSummonerId) {
                    e.isCancelled = true
                    return
                }

                if (target is Tameable && target.isTamed && target.owner?.uniqueId == summonerUUID) {
                    e.isCancelled = true
                    return
                }
            }
        }
    }


}
