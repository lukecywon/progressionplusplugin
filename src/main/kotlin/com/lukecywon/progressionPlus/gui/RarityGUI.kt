package com.lukecywon.progressionPlus.ui

import com.lukecywon.progressionPlus.enums.Rarity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object RarityGUI {
    private val title = Component.text("📚 Select Item Rarity")
    fun open(player: Player) {
        val gui = Bukkit.createInventory(null, 9, title)

        gui.setItem(0, createConcrete(Material.WHITE_CONCRETE, Rarity.COMMON))
        gui.setItem(1, createConcrete(Material.LIME_CONCRETE, Rarity.UNCOMMON))
        gui.setItem(2, createConcrete(Material.LIGHT_BLUE_CONCRETE, Rarity.RARE))
        gui.setItem(3, createConcrete(Material.PURPLE_CONCRETE, Rarity.EPIC))
        gui.setItem(4, createConcrete(Material.YELLOW_CONCRETE, Rarity.LEGENDARY))

        gui.setItem(8, ItemStack(Material.BARRIER).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text("Close").color(NamedTextColor.RED))
            }
        })

        player.openInventory(gui)
    }

    private fun createConcrete(material: Material, rarity: Rarity): ItemStack {
        return ItemStack(material).apply {
            itemMeta = itemMeta?.apply {
                displayName(Component.text(rarity.displayName).color(rarity.color))
            }
        }
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title // returns a String (yes, deprecated)
        if (title != "📚 Select Item Rarity") return

        e.isCancelled = true
        val clicked = e.currentItem ?: return

        when (clicked.type) {
            Material.WHITE_CONCRETE     -> ItemListGUI.open(player, Rarity.COMMON)
            Material.LIME_CONCRETE      -> ItemListGUI.open(player, Rarity.UNCOMMON)
            Material.LIGHT_BLUE_CONCRETE -> ItemListGUI.open(player, Rarity.RARE)
            Material.PURPLE_CONCRETE    -> ItemListGUI.open(player, Rarity.EPIC)
            Material.YELLOW_CONCRETE    -> ItemListGUI.open(player, Rarity.LEGENDARY)
            Material.BARRIER            -> player.closeInventory()
            else -> return
        }

        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
    }
}
