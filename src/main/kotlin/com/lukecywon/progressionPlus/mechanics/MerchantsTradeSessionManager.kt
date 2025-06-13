package com.lukecywon.progressionPlus.mechanics

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import java.util.*

object MerchantsTradeSessionManager {
    private val activeTrades = mutableMapOf<UUID, TradeSession>()

    private val p1Slots = listOf(10, 11, 12, 19, 20, 21)
    private val p2Slots = listOf(14, 15, 16, 23, 24, 25)
    private const val p1AcceptSlot = 22
    private const val p2AcceptSlot = 31

    private const val GUI_TITLE_PREFIX = "Trading: "

    fun openTradeGUI(p1: Player, p2: Player) {
        val title = "$GUI_TITLE_PREFIX${p1.name} ↔ ${p2.name}"
        val inv = Bukkit.createInventory(TradeHolder(p1, p2), 54, title)

        inv.setItem(p1AcceptSlot, acceptButton("§eAccept Trade"))
        inv.setItem(p2AcceptSlot, acceptButton("§eAccept Trade"))

        val session = TradeSession(p1, p2, inv)
        activeTrades[p1.uniqueId] = session
        activeTrades[p2.uniqueId] = session

        p1.openInventory(inv)
        p2.openInventory(inv)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val session = activeTrades[player.uniqueId] ?: return
        if (!e.view.title.startsWith(GUI_TITLE_PREFIX)) return
        if (e.inventory != session.inventory) return

        e.isCancelled = true
        val slot = e.rawSlot

        // Accept button logic
        if (slot == p1AcceptSlot || slot == p2AcceptSlot) {
            session.markAccepted(player)
            player.sendMessage("§aYou marked your trade as ready.")
            player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
            session.updateAcceptButtons()
            if (session.isBothAccepted()) finishTrade(session)
            return
        }

        // Reset acceptance if they modify offer
        if ((player == session.p1 && slot in p1Slots) || (player == session.p2 && slot in p2Slots)) {
            session.resetAcceptance()
            session.updateAcceptButtons()
        }
    }

    fun handleClose(e: InventoryCloseEvent) {
        val player = e.player as? Player ?: return
        val session = activeTrades[player.uniqueId] ?: return
        if (e.inventory != session.inventory) return

        session.p1.sendMessage("§cTrade canceled.")
        session.p2.sendMessage("§cTrade canceled.")
        session.p1.playSound(session.p1.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)
        session.p2.playSound(session.p2.location, Sound.ENTITY_VILLAGER_NO, 1f, 1f)

        activeTrades.remove(session.p1.uniqueId)
        activeTrades.remove(session.p2.uniqueId)
    }

    private fun finishTrade(session: TradeSession) {
        val inv = session.inventory
        val p1Items = getOffer(inv, p1Slots)
        val p2Items = getOffer(inv, p2Slots)

        p1Slots.forEach { inv.setItem(it, null) }
        p2Slots.forEach { inv.setItem(it, null) }

        p1Items.forEach { giveSafely(session.p2, it) }
        p2Items.forEach { giveSafely(session.p1, it) }

        session.p1.sendMessage("§aTrade complete!")
        session.p2.sendMessage("§aTrade complete!")
        session.p1.playSound(session.p1.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f)
        session.p2.playSound(session.p2.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.5f)

        session.p1.closeInventory()
        session.p2.closeInventory()

        activeTrades.remove(session.p1.uniqueId)
        activeTrades.remove(session.p2.uniqueId)
    }

    private fun getOffer(inv: Inventory, slots: List<Int>): List<ItemStack> {
        return slots.mapNotNull { inv.getItem(it)?.clone() }
    }

    private fun acceptButton(displayName: String): ItemStack {
        return ItemStack(Material.LIME_DYE).apply {
            val meta = itemMeta
            meta.setDisplayName(displayName)
            itemMeta = meta
        }
    }

    private fun giveSafely(player: Player, item: ItemStack) {
        val leftover = player.inventory.addItem(item)
        if (leftover.isNotEmpty()) {
            leftover.values.forEach { player.world.dropItemNaturally(player.location, it) }
        }
    }

    private class TradeHolder(val p1: Player, val p2: Player) : InventoryHolder {
        override fun getInventory(): Inventory = Bukkit.createInventory(null, 0)
    }

    private class TradeSession(
        val p1: Player,
        val p2: Player,
        val inventory: Inventory,
        private var p1Accepted: Boolean = false,
        private var p2Accepted: Boolean = false,
    ) {
        fun markAccepted(player: Player) {
            if (player == p1) p1Accepted = true
            if (player == p2) p2Accepted = true
        }

        fun resetAcceptance() {
            p1Accepted = false
            p2Accepted = false
        }

        fun isBothAccepted(): Boolean = p1Accepted && p2Accepted

        fun updateAcceptButtons() {
            inventory.setItem(p1AcceptSlot, acceptButton(if (p1Accepted) "§a✔ Accepted" else "§eAccept Trade"))
            inventory.setItem(p2AcceptSlot, acceptButton(if (p2Accepted) "§a✔ Accepted" else "§eAccept Trade"))
        }
    }
}
