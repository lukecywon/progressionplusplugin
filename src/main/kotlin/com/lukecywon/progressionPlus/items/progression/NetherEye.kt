package com.lukecywon.progressionPlus.items.progression

import com.lukecywon.progressionPlus.ProgressionPlus
import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object NetherEye : CustomItem("nether_eye", Rarity.PROGRESSION) {

    override fun createItemStack(): ItemStack {
        val item = ItemStack(Material.NETHER_STAR)
        val meta = item.itemMeta

        meta.displayName(
            Component.text("Nether Eye")
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
        )

        meta.lore(
            listOf(
                ItemLore.description("A forbidden construct forged from shards and core essence."),
                ItemLore.description("Use it to toggle access to the Nether."),
                ItemLore.separator(),
                ItemLore.lore("“Only the worthy may unseal what lies beyond the flames.”")
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    fun toggleNether(player: Player) {
        val plugin = ProgressionPlus.getPlugin()
        val config = plugin.config

        val current = config.getBoolean("nether_unlocked", false)
        val newValue = !current
        config.set("nether_unlocked", newValue)
        plugin.saveConfig()

        if (newValue) {
            player.sendMessage("§c§lNether access has been §aENABLED§c.")
        } else {
            player.sendMessage("§e§lNether access has been §cDISABLED§e.")
        }
    }
}
