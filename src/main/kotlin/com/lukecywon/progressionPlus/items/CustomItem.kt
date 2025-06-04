package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

abstract class CustomItem(private val name: String, private val rarity: Rarity) {
    protected val key: NamespacedKey
    protected val plugin: JavaPlugin

    init {
        plugin = ProgressionPlus.getPlugin()
        key = NamespacedKey(plugin, name)
        CustomItemRegistry.register(name, this)
    }

    abstract fun createItemStack(): ItemStack

    open fun applyMeta(item: ItemStack): ItemStack {
        val meta = item.itemMeta ?: return item

        // Add rarity tag
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.persistentDataContainer.set(
            NamespacedKey(ProgressionPlus.getPlugin(), "rarity"),
            PersistentDataType.STRING,
            rarity.name
        )

        // Set display name or lore with rarity color
        val lore = meta.lore()?.toMutableList() ?: mutableListOf()
        lore.add(Component.text(""))
        lore.add(Component.text(getRarity().displayName).color(getRarity().color).decorate(TextDecoration.BOLD))
        meta.lore(lore)

        meta.persistentDataContainer.set(NamespacedKey(plugin, "unique_id"), PersistentDataType.STRING, UUID.randomUUID().toString())

        item.itemMeta = meta
        return item
    }

    fun isThisItem(item: ItemStack?): Boolean {
        val meta = item?.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    fun getRarity(): Rarity {
        return rarity
    }

    companion object {
        private val cooldowns: MutableMap<Pair<String, UUID>, Long> = mutableMapOf()

        fun setCooldown(itemId: String, playerId: UUID, durationMillis: Long) {
            cooldowns[itemId to playerId] = System.currentTimeMillis() + durationMillis
        }

        fun isOnCooldown(itemId: String, playerId: UUID): Boolean {
            val expiry = cooldowns[itemId to playerId] ?: return false
            return System.currentTimeMillis() < expiry
        }

        fun getCooldownRemaining(itemId: String, playerId: UUID): Long {
            val expiry = cooldowns[itemId to playerId] ?: return 0
            return (expiry - System.currentTimeMillis()).coerceAtLeast(0)
        }

        fun clearCooldowns(playerId: UUID) {
            cooldowns.keys.removeIf { it.second == playerId }
        }
    }
}