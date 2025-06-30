package com.lukecywon.progressionPlus.gui

import com.lukecywon.progressionPlus.ProgressionPlus
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object MerchantsTradeGUI {
    private val tradeSessions = mutableMapOf<UUID, TradeSession>()
    private var completed = false

    private const val GUI_TITLE = "🛒 Trading Session"
    private const val INVENTORY_SIZE = 54
    private val separatorSlots = listOf(4, 13, 22, 31, 40)
    internal val player1Slots = listOf(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21, 27, 28, 29, 30, 36, 37, 38, 39)
    internal val player2Slots = listOf(5, 6, 7, 8, 14, 15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35, 41, 42, 43, 44)

    private const val ACCEPT_P1_SLOT = 45
    private const val CANCEL_P1_SLOT = 46
    private const val COUNTDOWN_SLOT = 49
    private const val CANCEL_P2_SLOT = 52
    private const val ACCEPT_P2_SLOT = 53

    fun getSession(player: Player): TradeSession? {
        return tradeSessions[player.uniqueId]
    }

    fun open(player1: Player, player2: Player) {
        val guiTitle = "🛒 ${player1.name} ⇄ ${player2.name}"
        val gui = Bukkit.createInventory(null, INVENTORY_SIZE, guiTitle)
        val session = TradeSession(player1, player2, gui)

        tradeSessions[player1.uniqueId] = session
        tradeSessions[player2.uniqueId] = session

        separatorSlots.forEach { gui.setItem(it, blackPane()) }
        gui.setItem(ACCEPT_P1_SLOT, greenConcrete("Accept Trade"))
        gui.setItem(CANCEL_P1_SLOT, redConcrete("Cancel Trade"))
        gui.setItem(COUNTDOWN_SLOT, grayDye("Awaiting Acceptance"))
        gui.setItem(CANCEL_P2_SLOT, redConcrete("Cancel Trade"))
        gui.setItem(ACCEPT_P2_SLOT, greenConcrete("Accept Trade"))

        player1.openInventory(gui)
        player2.openInventory(gui)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (e.view.title != GUI_TITLE) return

        val session = tradeSessions[player.uniqueId] ?: return
        val isPlayer1 = player.uniqueId == session.player1.uniqueId
        val rawSlot = e.rawSlot
        val guiSize = e.view.topInventory.size

        val allowedSlots = if (isPlayer1) player1Slots.toSet() else player2Slots.toSet()
        val allowedButtons = if (isPlayer1) setOf(ACCEPT_P1_SLOT, CANCEL_P1_SLOT) else setOf(ACCEPT_P2_SLOT, CANCEL_P2_SLOT)

        if (e.click.isShiftClick) {
            val clickedInv = e.clickedInventory ?: return
            val item = e.currentItem?.clone() ?: return
            val destSlots = if (isPlayer1) player1Slots else player2Slots
            val isInPlayerInventory = clickedInv == e.view.bottomInventory
            val isInTradeArea = rawSlot in destSlots

            if (isInPlayerInventory) {
                for (slot in destSlots) {
                    val existing = e.view.getItem(slot)
                    if (existing == null || existing.type == Material.AIR) {
                        e.view.setItem(slot, item)
                        e.currentItem = null
                        session.cancelCountdown()
                        e.isCancelled = true
                        return
                    }
                }
                e.isCancelled = true
            } else if (isInTradeArea) {
                val result = player.inventory.addItem(item)
                if (result.isEmpty()) {
                    e.view.setItem(rawSlot, null)
                    session.cancelCountdown()
                }
                e.isCancelled = true
            } else {
                e.isCancelled = true
            }

            return
        }

        if (rawSlot >= guiSize) return
        if (rawSlot !in allowedSlots && rawSlot !in allowedButtons) {
            e.isCancelled = true
            return
        }

        e.isCancelled = false

        when (rawSlot) {
            ACCEPT_P1_SLOT -> {
                e.isCancelled = true
                session.setAccepted(session.player1)
            }
            ACCEPT_P2_SLOT -> {
                e.isCancelled = true
                session.setAccepted(session.player2)
            }
            CANCEL_P1_SLOT, CANCEL_P2_SLOT -> {
                e.isCancelled = true
                session.cancelTrade()
            }
            else -> session.cancelCountdown()
        }

        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1.2f)
    }

    private fun greenConcrete(name: String) = ItemStack(Material.GREEN_CONCRETE).apply {
        itemMeta = itemMeta.apply { displayName(Component.text("§a$name")) }
    }

    private fun redConcrete(name: String) = ItemStack(Material.RED_CONCRETE).apply {
        itemMeta = itemMeta.apply { displayName(Component.text("§c$name")) }
    }

    private fun greenGlassPane(name: String) = ItemStack(Material.GREEN_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta.apply { displayName(Component.text("§aWaiting for other player...")) }
    }

    private fun yellowDye(count: Int) = ItemStack(Material.YELLOW_DYE, count).apply {
        itemMeta = itemMeta.apply { displayName(Component.text("§eFinalizing in $count...")) }
    }

    private fun grayDye(name: String) = ItemStack(Material.GRAY_DYE).apply {
        itemMeta = itemMeta.apply { displayName(Component.text("§7$name")) }
    }

    private fun blackPane() = ItemStack(Material.BLACK_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta.apply { displayName(Component.text(" ")) }
    }

    class TradeSession(
        val player1: Player,
        val player2: Player,
        internal val gui: Inventory // <- made accessible
    ) {
        private var accepted1 = false
        private var accepted2 = false
        private var countdownTask: BukkitRunnable? = null

        fun setAccepted(player: Player) {
            if (player.uniqueId == player1.uniqueId) {
                accepted1 = true
                gui.setItem(ACCEPT_P1_SLOT, greenGlassPane("Waiting"))
            } else {
                accepted2 = true
                gui.setItem(ACCEPT_P2_SLOT, greenGlassPane("Waiting"))
            }
            if (accepted1 && accepted2) startCountdown()
        }

        fun cancelTrade(skipClose: Boolean = false) {
            if (completed) return // ✅ Don't cancel a completed trade

            player1.sendMessage("§cTrade cancelled.")
            player2.sendMessage("§cTrade cancelled.")

            player1Slots.forEach { slot ->
                gui.getItem(slot)?.takeIf { it.type != Material.AIR }?.let {
                    player1.inventory.addItem(it)
                    gui.setItem(slot, null)
                }
            }
            player2Slots.forEach { slot ->
                gui.getItem(slot)?.takeIf { it.type != Material.AIR }?.let {
                    player2.inventory.addItem(it)
                    gui.setItem(slot, null)
                }
            }

            if (!skipClose) {
                Bukkit.getScheduler().runTask(ProgressionPlus.getPlugin(), Runnable {
                    player1.closeInventory()
                    player2.closeInventory()
                })
            }

            tradeSessions.remove(player1.uniqueId)
            tradeSessions.remove(player2.uniqueId)
        }

        fun cancelCountdown() {
            if (accepted1 || accepted2) {
                accepted1 = false
                accepted2 = false
                gui.setItem(ACCEPT_P1_SLOT, greenConcrete("Accept Trade"))
                gui.setItem(ACCEPT_P2_SLOT, greenConcrete("Accept Trade"))
                gui.setItem(COUNTDOWN_SLOT, grayDye("Awaiting Acceptance"))
                countdownTask?.cancel()
                countdownTask = null
            }
        }

        private fun startCountdown() {
            var secondsLeft = 3
            countdownTask = object : BukkitRunnable() {
                override fun run() {
                    if (secondsLeft == 0) {
                        completeTrade()
                        return
                    }
                    gui.setItem(COUNTDOWN_SLOT, yellowDye(secondsLeft))
                    player1.playSound(player1.location, Sound.BLOCK_DISPENSER_DISPENSE, 1f, 1f)
                    player2.playSound(player2.location, Sound.BLOCK_DISPENSER_DISPENSE, 1f, 1f)
                    secondsLeft--
                }
            }.also { it.runTaskTimer(ProgressionPlus.getPlugin(), 0L, 20L) }
        }

        private fun completeTrade() {
            val items1 = player1Slots.mapNotNull(gui::getItem)
            val items2 = player2Slots.mapNotNull(gui::getItem)
            completed = true

            player1Slots.forEach { gui.setItem(it, null) }
            player2Slots.forEach { gui.setItem(it, null) }

            // Give player2's items to player1
            val overflow1 = player1.inventory.addItem(*items2.toTypedArray())
            // Give player1's items to player2
            val overflow2 = player2.inventory.addItem(*items1.toTypedArray())

            // Drop overflow items
            overflow1.values.forEach { player1.world.dropItemNaturally(player1.location, it) }
            overflow2.values.forEach { player2.world.dropItemNaturally(player2.location, it) }

            player1.sendMessage("§aTrade completed!")
            player2.sendMessage("§aTrade completed!")
            player1.closeInventory()
            player2.closeInventory()

            countdownTask?.cancel()
            countdownTask = null

            tradeSessions.remove(player1.uniqueId)
            tradeSessions.remove(player2.uniqueId)
        }

    }

    fun forceCancel(uuid: UUID) {
        tradeSessions[uuid]?.cancelTrade(skipClose = true)
    }

    fun handleClose(closingPlayer: Player) {
        val session = tradeSessions[closingPlayer.uniqueId] ?: return
        val other = if (closingPlayer.uniqueId == session.player1.uniqueId) session.player2 else session.player1

        session.cancelTrade(skipClose = false)

        Bukkit.getScheduler().runTask(ProgressionPlus.getPlugin(), Runnable {
            if (other.openInventory.topInventory == session.gui) {
                other.closeInventory()
            }
        })
    }


}
