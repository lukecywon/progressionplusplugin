package com.lukecywon.progressionPlus.utils

import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.utils.enums.Rarity
import java.util.*

object GUIHistory {
    private val historyMap = mutableMapOf<UUID, Deque<CustomItem>>()
    private val itemListMap = mutableMapOf<UUID, Rarity>()

    fun push(playerId: UUID, item: CustomItem) {
        val stack = historyMap.getOrPut(playerId) { ArrayDeque() }
        if (stack.peekLast() != item) {
            stack.addLast(item)
        }
    }

    fun pop(playerId: UUID): CustomItem? {
        val stack = historyMap[playerId] ?: return null
        if (stack.isNotEmpty()) stack.removeLast()
        return stack.peekLast()
    }

    fun peek(playerId: UUID): CustomItem? {
        return historyMap[playerId]?.peekLast()
    }

    fun clearStack(playerId: UUID) {
        historyMap[playerId]?.clear()
    }

    fun clearAll(playerId: UUID) {
        historyMap[playerId]?.clear()
        itemListMap.remove(playerId)
    }

    fun setItemListOrigin(playerId: UUID, rarity: Rarity) {
        itemListMap[playerId] = rarity
    }

    fun getItemListOrigin(playerId: UUID): Rarity? {
        return itemListMap[playerId]
    }
}
