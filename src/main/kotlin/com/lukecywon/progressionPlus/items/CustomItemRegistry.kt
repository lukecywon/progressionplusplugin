package com.lukecywon.progressionPlus.items

object CustomItemRegistry {
    private val items = mutableMapOf<String, CustomItem>()

    fun register(name: String, item: CustomItem) {
        items[name.lowercase()] = item
    }

    fun getAllNames(): List<String> = items.keys.toList()
    fun getItem(name: String): CustomItem? = items[name.lowercase()]

    fun getAll(): List<CustomItem> = items.values.toList()
}