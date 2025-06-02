package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

abstract class CustomItem(name: String) {
    protected val key: NamespacedKey

    init {
        val plugin = ProgressionPlus.getPlugin()
        key = NamespacedKey(plugin, name)
    }

    abstract fun createItemStack(): ItemStack

    fun isThisItem(item: ItemStack?): Boolean {
        val meta = item?.itemMeta ?: return false
        return meta.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }
}