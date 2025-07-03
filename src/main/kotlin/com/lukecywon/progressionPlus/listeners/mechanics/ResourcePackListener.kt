package com.yourpackage.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import net.kyori.adventure.text.Component

class ResourcePackListener : Listener {

    private val resourcePackUrl = "https://github.com/lukecywon/ProgressionPlusTexturepack/releases/download/release-1.0/ProgressionPlusTexturepack.zip"

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.setResourcePack(resourcePackUrl)
    }

    @EventHandler
    fun onResourcePackStatus(event: PlayerResourcePackStatusEvent) {
        val player = event.player
        when (event.status) {
            PlayerResourcePackStatusEvent.Status.ACCEPTED -> {
                player.sendMessage(Component.text("§eResource pack accepted."))
            }
            PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED -> {
                player.sendMessage(Component.text("§aResource pack successfully loaded!"))
            }
            PlayerResourcePackStatusEvent.Status.DECLINED -> {
                player.kick(Component.text("§cYou must accept the resource pack to play."))
            }
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD -> {
                player.sendMessage(Component.text("§cFailed to download resource pack."))
            }
            else -> {}
        }
    }
}
