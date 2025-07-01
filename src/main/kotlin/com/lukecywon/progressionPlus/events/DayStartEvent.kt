package com.lukecywon.progressionPlus.events

import org.bukkit.World
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class DayStartEvent(val world: World) : Event() {
    override fun getHandlers(): HandlerList = handlerList
    companion object {
        @JvmStatic val handlerList = HandlerList()
    }
}