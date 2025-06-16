package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.items.armor.common.WoodenBoots
import com.lukecywon.progressionPlus.items.armor.common.WoodenChestplate
import com.lukecywon.progressionPlus.items.armor.common.WoodenHelmet
import com.lukecywon.progressionPlus.items.armor.common.WoodenLeggings
import com.lukecywon.progressionPlus.items.armor.legendary.TwilightCrown
import com.lukecywon.progressionPlus.items.armor.uncommon.NocturnHood
import com.lukecywon.progressionPlus.items.component.*
import com.lukecywon.progressionPlus.items.utility.common.*
import com.lukecywon.progressionPlus.items.utility.epic.*
import com.lukecywon.progressionPlus.items.utility.epic.LuckTalisman
import com.lukecywon.progressionPlus.items.progression.NetherEye
import com.lukecywon.progressionPlus.items.utility.legendary.SacrificialClock
import com.lukecywon.progressionPlus.items.utility.legendary.SnowGlobe
import com.lukecywon.progressionPlus.items.utility.rare.*
import com.lukecywon.progressionPlus.items.utility.uncommon.EnchantmentExtractor
import com.lukecywon.progressionPlus.items.utility.uncommon.MaxHeartFruit
import com.lukecywon.progressionPlus.items.utility.uncommon.PhantomCharm
import com.lukecywon.progressionPlus.items.weapons.epic.*
import com.lukecywon.progressionPlus.items.weapons.legendary.EchoGun
import com.lukecywon.progressionPlus.items.weapons.legendary.VoidReaper
import com.lukecywon.progressionPlus.items.weapons.rare.*
import com.lukecywon.progressionPlus.items.weapons.uncommon.BerserkerSword
import com.lukecywon.progressionPlus.items.weapons.uncommon.RogueSword
import com.lukecywon.progressionPlus.items.weapons.uncommon.VenomDagger
import com.lukecywon.progressionPlus.items.weapons.uncommon.VerdantCleaver

object AllItems {
    val allItems = mutableListOf<CustomItem>()            // Optional: track all CustomItems

    fun registerAll() {
        fun reg(item: CustomItem) {
            allItems.add(item)
        }

        reg(MaxHeartFruit)
        reg(EchoGun)
        reg(BerserkerSword)
        reg(FlightBeacon)
        reg(HasteBanner)
        reg(SpeedBanner)
        reg(AbsorptionBanner)
        reg(RegenBanner)
        reg(JumpBanner)
        reg(HealthCrystal)
        reg(RecallPotion)
        reg(PhoenixTotem)
        reg(WormholePotion)
        reg(SnowGlobe)
        reg(RogueSword)
        reg(VenomDagger)
        reg(AshenWarhammer)
        reg(ResonantBlade)
        reg(FerociousBlade)
        reg(SoulPiercer)
        reg(LuckTalisman)
        reg(VoidReaper)
        reg(SoulrendScythe)
        reg(NocturnHood)
        reg(TwilightCrown)
        reg(TribalSpear)
        reg(OldKingsBlade)
        reg(SacrificialClock)
        reg(ShadowKatana)
        reg(WoodenHelmet)
        reg(WoodenChestplate)
        reg(WoodenLeggings)
        reg(WoodenBoots)
        reg(ExecutionerSword)
        reg(PhantomCharm)
        reg(VerdantCleaver)
        reg(TectonicFang)
        reg(EarthSplitter)
        reg(EarthshatterHammer)
        reg(NetherEye)
        reg(HeadsmansEdge)
        reg(ContainmentSigil)
        reg(EnchantmentExtractor)
        reg(AshbornePendant)
        reg(ItemEncyclopedia)
        reg(MerchantsContract)
        reg(AbyssalBox)
        reg(BuilderWand)
        reg(Peacemaker)
        reg(RefinedEye)
        reg(DesertAnkh)
        reg(EchoCore)
        reg(TwistedRoot)
        reg(TideCrystal)
        reg(WardensHeart)
        reg(InfernalShard)
        reg(AetherCore)
    }
}
