package com.lukecywon.progressionPlus.items

import com.destroystokyo.paper.profile.PlayerProfile
import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.Bukkit
import org.bukkit.profile.PlayerTextures
import java.net.URL
import java.util.*

abstract class CustomItem(private val name: String, private val rarity: Rarity, private val stackable: Boolean = false) {
    val key: NamespacedKey
    protected val plugin: JavaPlugin

    init {
        plugin = ProgressionPlus.getPlugin()
        key = NamespacedKey(plugin, name)
        CustomItemRegistry.register(name, this)
    }

    abstract fun createItemStack(): ItemStack

    open fun getRecipe(): List<RecipeChoice?>? {
        return null
    }

    fun hasRecipe(): Boolean {
        return getRecipe()?.any { it != null } == true
    }

    open fun applyMeta(item: ItemStack): ItemStack {
        val meta = item.itemMeta ?: return item

        // Add rarity tag
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        meta.persistentDataContainer.set(
            NamespacedKey(ProgressionPlus.getPlugin(), "rarity"),
            PersistentDataType.STRING,
            rarity.name
        )

        meta.isUnbreakable = true
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

        // Set display name or lore with rarity color
        val lore = meta.lore()?.toMutableList() ?: mutableListOf()
        lore.add(Component.text(getRarity().displayName).color(getRarity().color).decorate(TextDecoration.BOLD))
        meta.lore(lore)

        if (!stackable) {
            meta.persistentDataContainer.set(
                NamespacedKey(plugin, "unique_id"),
                PersistentDataType.STRING,
                UUID.randomUUID().toString()
            )
        }

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

    open fun getExtraInfo(): List<String> {
        return emptyList() // By default, no extra info
    }

    open fun applyBaseDamage(item: ItemStack, newBaseDamage: Double? = null): ItemStack {
        val meta = item.itemMeta ?: return item.clone()
        val baseDamage = ItemLore.getBaseStats(item.type).first
        val finalBase = newBaseDamage ?: baseDamage
        val difference = finalBase - baseDamage

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), "damage"),
            difference,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier)
        item.itemMeta = meta

        return item
    }

    open fun applyBaseAttackSpeed(item: ItemStack, newBaseSpeed: Double? = null): ItemStack {
        val meta = item.itemMeta ?: return item.clone()
        val baseSpeed = ItemLore.getBaseStats(item.type).second
        val finalSpeed = newBaseSpeed ?: baseSpeed
        val difference = finalSpeed - baseSpeed

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), "attack_speed"),
            difference,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, modifier)
        item.itemMeta = meta

        return item
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

        fun createCustomHead(url: String): ItemStack {
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            val validUrl = URL(url)

            // Create empty profile with random UUID
            val profile: PlayerProfile = Bukkit.createPlayerProfile(UUID.randomUUID()) as PlayerProfile

            // Set the base64 texture
            val textures: PlayerTextures = profile.textures
            textures.skin = validUrl
            profile.setTextures(textures)

            // Apply the profile to the item
            meta.playerProfile = profile

            // Make head unplaceable
            meta.persistentDataContainer.set(
                NamespacedKey(ProgressionPlus.getPlugin(), "wearable_head"),
                PersistentDataType.BYTE,
                1
            )

            head.itemMeta = meta

            return head
        }

        fun isCustomHead(item: ItemStack): Boolean {
            val meta = item.itemMeta ?: return false

            return meta.persistentDataContainer.has(
                NamespacedKey(ProgressionPlus.getPlugin(), "wearable_head"),
                PersistentDataType.BYTE
            )
        }
    }
}