package com.lukecywon.progressionPlus

import com.lukecywon.progressionPlus.commands.*
import com.lukecywon.progressionPlus.listeners.*
import com.lukecywon.progressionPlus.items.*
import com.lukecywon.progressionPlus.listeners.armor.epic.RocketHarnessListener
import com.lukecywon.progressionPlus.listeners.armor.legendary.TwilightCrownListener
import com.lukecywon.progressionPlus.listeners.armor.uncommon.NocturnHoodListener
import com.lukecywon.progressionPlus.listeners.loot.ElderGuardianDropListener
import com.lukecywon.progressionPlus.listeners.loot.StructureLootListener
import com.lukecywon.progressionPlus.listeners.mechanics.*
import com.lukecywon.progressionPlus.listeners.utility.CustomItemVanillaBlocker
import com.lukecywon.progressionPlus.listeners.utility.common.BannerListener
import com.lukecywon.progressionPlus.listeners.utility.common.ItemEncyclopediaListener
import com.lukecywon.progressionPlus.listeners.utility.epic.*
import com.lukecywon.progressionPlus.listeners.utility.epic.LuckTalismanListener
import com.lukecywon.progressionPlus.listeners.weapons.legendary.BoomerangBladeListener
import com.lukecywon.progressionPlus.listeners.utility.legendary.NetherEyeListener
import com.lukecywon.progressionPlus.listeners.utility.legendary.SnowGlobeListener
import com.lukecywon.progressionPlus.listeners.utility.rare.*
import com.lukecywon.progressionPlus.listeners.utility.uncommon.EnchantmentExtractorListener
import com.lukecywon.progressionPlus.listeners.utility.uncommon.PhantomCharmListener
import com.lukecywon.progressionPlus.listeners.weapons.epic.*
import com.lukecywon.progressionPlus.listeners.weapons.legendary.EchoGunListener
import com.lukecywon.progressionPlus.listeners.weapons.legendary.FamesAuriListener
import com.lukecywon.progressionPlus.listeners.weapons.legendary.VoidReaperListener
import com.lukecywon.progressionPlus.listeners.weapons.rare.*
import com.lukecywon.progressionPlus.listeners.weapons.uncommon.BerserkerSwordListener
import com.lukecywon.progressionPlus.listeners.weapons.uncommon.RogueSwordListener
import com.lukecywon.progressionPlus.listeners.weapons.uncommon.VenomDaggerListener
import com.lukecywon.progressionPlus.listeners.utility.uncommon.VerdantCleaverListener
import com.lukecywon.progressionPlus.listeners.weapons.legendary.GravityMaulListener
import com.lukecywon.progressionPlus.mechanics.*
import com.lukecywon.progressionPlus.recipes.*

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

class Initialize(private val plugin: JavaPlugin) {
    init {
        AllItems.registerAll()
        commands()
        listeners()
        recipes()
        mechanics()
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
        plugin.getCommand("config")?.setExecutor(ConfigCommand())

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
            FamesAuriListener(),
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
            RecipeUnlockListener(),
            CraftRestrictListener(),
            AdvancementRecipeUnlockListener(),
            RecipeBlockerListener(),
            CustomItemVanillaBlocker(),
            VillagerTradeListener(),
            BoomerangBladeListener(),
            ExecutionerSwordListener(),
            TribalSpearListener(),
            RocketHarnessListener(),
            ParagonShieldListener(),
            GravityMaulListener(),
            WearableHeadListener()
        )

        listeners.forEach {
            plugin.server.pluginManager.registerEvents(it, plugin)
        }
    }

    private fun recipes() {
        AllItems.allItems.forEach {
            // Only add items with non-empty recipes
            if (RecipeGenerator.generateRecipe(it) == null) return@forEach

            val recipe = RecipeGenerator.generateRecipe(it)
            val key = it.key

            if (Bukkit.getRecipe(key) != null) {
                Bukkit.removeRecipe(key)
            }

            Bukkit.addRecipe(recipe)
        }

        // Add custom recipe overrides
        val customRecipes: List<Recipe> = listOf(
            EyeOfEnderRecipe,
            EnderChestRecipe
        )

        customRecipes.forEach { recipe ->
            recipe.register()
        }

        val diamondRecipes = listOf(
            NamespacedKey.minecraft("diamond_sword"),
            NamespacedKey.minecraft("diamond_pickaxe"),
            NamespacedKey.minecraft("diamond_axe"),
            NamespacedKey.minecraft("diamond_shovel"),
            NamespacedKey.minecraft("diamond_hoe"),
            NamespacedKey.minecraft("diamond_helmet"),
            NamespacedKey.minecraft("diamond_chestplate"),
            NamespacedKey.minecraft("diamond_leggings"),
            NamespacedKey.minecraft("diamond_boots")
        )

        if (!plugin.config.getBoolean("diamond-unlocked")) {
            // Remove diamond crafts
            diamondRecipes.forEach { item ->
                Bukkit.removeRecipe(item)
            }
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