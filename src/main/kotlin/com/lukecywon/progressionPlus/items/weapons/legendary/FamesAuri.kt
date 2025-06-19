package com.lukecywon.progressionPlus.items.weapons.legendary

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object FamesAuri : CustomItem("fames_auri", Rarity.LEGENDARY) {

    private val activePlayers = ConcurrentHashMap<UUID, Int>()
    private val scheduledTasks = ConcurrentHashMap<UUID, BukkitRunnable>()
    private const val INTERVAL_TICKS = 60L // 3 seconds

    data class BuffConfig(
        val material: Material,
        val potionEffects: List<PotionEffect> = listOf(),
        val attributeModifiers: List<Pair<Attribute, AttributeModifier>> = listOf(),
        val label: String
    )

    val buffs = listOf(
        BuffConfig(
            Material.GOLD_NUGGET,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 0)),
            label = "Speed I"
        ),
        BuffConfig(
            Material.GOLD_INGOT,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 1)),
            label = "Speed II"
        ),
        BuffConfig(
            Material.GOLD_BLOCK,
            listOf(PotionEffect(PotionEffectType.SPEED, 80, 3)),
            label = "Speed IV"
        )
    )

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.GOLDEN_SWORD)
        val meta = item.itemMeta
        meta.setDisplayName("§6Fames Auri")
        item.itemMeta = meta
        return applyMeta(item)
    }

    fun toggle(player: Player) {
        val uuid = player.uniqueId
        if (activePlayers.containsKey(uuid)) {
            deactivate(player)
            return
        }

        val mode = determineBuffMode(player) ?: run {
            player.sendMessage("§cNo gold material to consume.")
            return
        }

        activePlayers[uuid] = mode
        player.sendMessage("§eFames Auri activating in 3 seconds using ${buffs[mode].label}...")

        object : BukkitRunnable() {
            override fun run() {
                if (!player.isOnline || !isFamesAuri(player.inventory.itemInMainHand)) {
                    deactivate(player)
                    return
                }

                val task = object : BukkitRunnable() {
                    override fun run() {
                        if (!player.isOnline || !isFamesAuri(player.inventory.itemInMainHand)) {
                            deactivate(player)
                            return
                        }

                        val config = buffs[mode]
                        if (!player.inventory.contains(config.material)) {
                            player.sendMessage("§cOut of ${config.material}.")
                            deactivate(player)
                            return
                        }

                        player.inventory.removeItem(ItemStack(config.material, 1))
                        config.potionEffects.forEach { player.addPotionEffect(it) }
                        config.attributeModifiers.forEach { (attr, mod) ->
                            player.getAttribute(attr)?.addModifier(mod)
                        }
                    }
                }
                task.runTaskTimer(ProgressionPlus.getPlugin(), 0L, INTERVAL_TICKS)
                scheduledTasks[uuid] = task
            }
        }.runTaskLater(ProgressionPlus.getPlugin(), INTERVAL_TICKS)
    }

    fun deactivate(player: Player) {
        val uuid = player.uniqueId
        activePlayers.remove(uuid)
        scheduledTasks.remove(uuid)?.cancel()

        // Clear any buffs
        buffs.flatMap { it.potionEffects }.forEach {
            player.removePotionEffect(it.type)
        }

        buffs.flatMap { it.attributeModifiers }.forEach { (attr, mod) ->
            player.getAttribute(attr)?.removeModifier(mod)
        }

        player.sendMessage("§6Fames Auri has been deactivated.")
    }

    fun cycleMode(player: Player) {
        val uuid = player.uniqueId
        val current = activePlayers[uuid] ?: 0
        val next = (current + 1) % buffs.size
        activePlayers[uuid] = next
        player.sendMessage("§eFames Auri switched to ${buffs[next].label}.")
    }

    fun stopIfUnequipped(player: Player) {
        if (!isFamesAuri(player.inventory.itemInMainHand)) {
            deactivate(player)
        }
    }

    fun isFamesAuri(item: ItemStack?): Boolean {
        return item?.let { isThisItem(it) && it.type == Material.GOLDEN_SWORD } ?: false
    }

    private fun determineBuffMode(player: Player): Int? {
        for ((index, config) in buffs.withIndex()) {
            if (player.inventory.contains(config.material)) return index
        }
        return null
    }
}
