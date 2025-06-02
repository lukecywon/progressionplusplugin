package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object FlightBeacon : CustomItem("flight_beacon", Rarity.EPIC) {
    val beaconKey = NamespacedKey("survivaltestplugin", "flight_beacon")

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.BEACON)
        val meta = item.itemMeta

        meta.displayName(Component.text("Flight Beacon").color(NamedTextColor.AQUA))
        meta.lore(
            listOf(
                Component.text("Place to grant flight in an area.").color(NamedTextColor.GRAY),
                Component.text("Only activates with diamond blocks below.").color(NamedTextColor.AQUA),
                Component.text("Area size increases with pyramid tier.").color(NamedTextColor.DARK_GRAY)
            )
        )

        meta.setCustomModelData(9003)
        meta.persistentDataContainer.set(beaconKey, PersistentDataType.BYTE, 1)

        item.itemMeta = meta
        return applyMeta(item)
    }

    fun isBeacon(item: ItemStack?): Boolean {
        if (item == null || item.type != Material.BEACON) return false
        val meta = item.itemMeta ?: return false
        return meta.persistentDataContainer.has(beaconKey, PersistentDataType.BYTE)
    }
}