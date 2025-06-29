package com.lukecywon.progressionPlus.setup

import com.lukecywon.progressionPlus.utils.annotations.RunOnEnable
import com.lukecywon.progressionPlus.commands.*
import org.bukkit.plugin.java.JavaPlugin

class SetupCommands {
    @RunOnEnable
    fun commands(plugin: JavaPlugin) {
        plugin.getCommand("artifact")?.setExecutor(ArtifactCommand())
        plugin.getCommand("artifact")?.tabCompleter = ArtifactTabCompleter()
        plugin.getCommand("fixme")?.setExecutor(FixMeCommand())
        plugin.getCommand("wormhole")?.setExecutor(WormholeCommand())
        plugin.getCommand("cooldown")?.setExecutor(CooldownCommand())
        plugin.getCommand("cooldown")?.tabCompleter = CooldownTabCompleter()
        plugin.getCommand("addsouls")?.setExecutor(AddSoulsCommand())
        plugin.getCommand("trade")?.setExecutor(MerchantsCommand())
        plugin.getCommand("config")?.setExecutor(ConfigCommand())
    }
}