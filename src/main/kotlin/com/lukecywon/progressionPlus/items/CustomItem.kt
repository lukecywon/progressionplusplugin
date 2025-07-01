package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.utils.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.NamespacedKey
import org.bukkit.attribute.AttributeModifier
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.*
import org.reflections.Reflections

import java.util.*

abstract class CustomItem(val name: String, private val rarity: Rarity, private val stackable: Boolean = false, private val enchantable: Boolean = true) {
    protected var plugin: JavaPlugin = ProgressionPlus.getPlugin()
    val key: NamespacedKey = NamespacedKey(plugin, name)

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

        // Set if an item will be enchantable
        meta.persistentDataContainer.set(
            NamespacedKey(plugin, "enchantable"),
            PersistentDataType.BOOLEAN,
            enchantable
        )

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
        val meta = item.itemMeta

        // Remove old modifiers
        meta.removeAttributeModifier(Attribute.ATTACK_DAMAGE)

        if (newBaseDamage == null) return item

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), "base_damage"),
            newBaseDamage!!,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier)
        item.itemMeta = meta

        return item
    }

    open fun applyBaseAttackSpeed(item: ItemStack, newBaseSpeed: Double? = null): ItemStack {
        val meta = item.itemMeta ?: return item.clone()

        val calculatedSpeed = newBaseSpeed?.minus(4)

        // Remove existing ATTACK_SPEED modifiers
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED)

        if (newBaseSpeed == null) return item

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), "attack_speed"),
            calculatedSpeed!!,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        )

        meta.addAttributeModifier(Attribute.ATTACK_SPEED, modifier)
        item.itemMeta = meta

        return item
    }

    open fun applyArmor(item: ItemStack, newArmor: Double? = null, slot: EquipmentSlotGroup = EquipmentSlotGroup.ARMOR): ItemStack {
        val meta = item.itemMeta

        // Remove old modifiers
        meta.removeAttributeModifier(Attribute.ARMOR)

        if (newArmor == null) return item

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), slot.toString() + "armor"),
            newArmor,
            AttributeModifier.Operation.ADD_NUMBER,
            slot
        )

        meta.addAttributeModifier(Attribute.ARMOR, modifier)
        item.itemMeta = meta

        return item
    }

    open fun applyArmorToughness(item: ItemStack, newArmorToughness: Double? = null, slot: EquipmentSlotGroup = EquipmentSlotGroup.ARMOR): ItemStack {
        val meta = item.itemMeta

        // Remove old modifiers
        meta.removeAttributeModifier(Attribute.ARMOR_TOUGHNESS)

        if (newArmorToughness == null) return item

        val modifier = AttributeModifier(
            NamespacedKey(ProgressionPlus.getPlugin(), "armor_toughness"),
            newArmorToughness!!,
            AttributeModifier.Operation.ADD_NUMBER,
            slot
        )

        meta.addAttributeModifier(Attribute.ARMOR_TOUGHNESS, modifier)
        item.itemMeta = meta

        return item
    }

    companion object {
        // Cooldown Management
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

        // Retrieval system for all CustomItems
        // using Reflection and classpath scanning
        private val allCustomItems = Reflections("com.lukecywon.progressionPlus.items")
        private val classes = allCustomItems.getSubTypesOf(CustomItem::class.java).sortedWith(compareBy({ it.name.substringBeforeLast('.') }, { it.simpleName }))

        fun getAll(): List<CustomItem> =
            classes.mapNotNull { it.kotlin.objectInstance }

        fun getAllNames(): List<String> =
            classes
                .mapNotNull { it.kotlin.objectInstance }
                .map { it.name }

        fun getItem(name: String): CustomItem? =
            classes
                .mapNotNull { it.kotlin.objectInstance }
                .firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}