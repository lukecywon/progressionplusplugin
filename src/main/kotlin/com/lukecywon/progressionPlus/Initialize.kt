package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.commands.*
import com.lukecywon.progressionPlus.listeners.*
import com.lukecywon.progressionPlus.items.*
import com.lukecywon.progressionPlus.mechanics.*
import com.lukecywon.progressionPlus.recipes.*
import com.yourplugin.listeners.NetherAccessListener

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Initialize(private val plugin: JavaPlugin) {
    init {
        commands()
        listeners()
        recipes()
        mechanics()
        AllItems.registerAll()
    }

    private fun commands() {
        plugin.getCommand("artifact")?.setExecutor(ArtifactCommand())
        plugin.getCommand("artifact")?.tabCompleter = ArtifactTabCompleter()
        plugin.getCommand("fixme")?.setExecutor(FixMeCommand())
        plugin.getCommand("wormhole")?.setExecutor(WormholeCommand())
        plugin.getCommand("cooldown")?.setExecutor(CooldownCommand())
        plugin.getCommand("cooldown")?.tabCompleter = CooldownTabCompleter()
        plugin.getCommand("addsouls")?.setExecutor(AddSoulsCommand())
        plugin.getCommand("trade")?.setExecutor(MerchantsCommand())

    }

    private fun listeners() {
        val listeners = listOf(
            EchoGunListener(),
            LegendaryItemListener(),
            MaxHeartFruitListener(),
            HealthCrystalListener(),
            BerserkerSwordListener(),
            FlightBeaconListener(),
            BannerListener(),
            RecallPotionListener(),
            PhoenixTotemListener(),
            WormholePotionListener(),
            WormholeGUIListener(),
            SnowGlobeListener(),
            HeartLossOnDeathListener(),
            RogueSwordListener(),
            VenomDaggerListener(),
            ResonantBladeListener(),
            AshenWarhammerListener(),
            FerociousBladeListener(),
            SoulPiercerListener(),
            LuckTalismanListener(),
            VoidReaperListener(),
            SacrificialClockListener(),
            SoulrendScytheListener(),
            NocturnHoodListener(),
            TwilightCrownListener(),
            OldKingsBladeListener(),
            ShadowKatanaListener(),
            PhantomCharmListener(),
            VerdantCleaverListener(),
            TectonicFangListener(),
            EarthSplitterListener(),
            EarthshatterHammerListener(),
            NetherAccessListener(),
            NetherEyeListener(),
            NetherPortalIgniteListener(),
            StructureLootListener(),
            ElderGuardianDropListener(),
            HeadsmansEdgeListener(),
            ContainmentSigilListener(),
            SpawnEggBlockerListener(),
            EnchantmentExtractorListener(),
            AshbornePendantListener(),
            MerchantsContractListener(),
            AbyssalBoxListener(),
            ItemEncyclopediaListener(),
            BuilderWandListener(),
            PeacemakerListener(),
        )

        listeners.forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    private fun recipes() {
        val customItemsWithRecipes = AllItems::class
            .members
            .filter { it.returnType.classifier == CustomItemWithRecipe::class }
            .mapNotNull { it.call(AllItems) as? CustomItemWithRecipe }

        customItemsWithRecipes.forEach {
            val recipe = RecipeGenerator.generateRecipe(it)
            val key = it.key

            if (Bukkit.getRecipe(key) != null) {
                Bukkit.removeRecipe(key)
            }

            Bukkit.addRecipe(recipe)
        }
    }


    private fun mechanics() {
        val mechanics = listOf<Manager>(
            BerserkerSwordManager,
            FlightBeaconManager,
            LegendaryManager
        )

        mechanics.forEach {
            it.start(plugin)
        }
    }
}