package com.lukecywon.progressionPlus.items

import com.lukecywon.progressionPlus.mechanics.CustomItemWithRecipe

object AllItems {
    val allItems = mutableListOf<CustomItem>()            // Optional: track all CustomItems
    val recipeItems = mutableListOf<CustomItemWithRecipe>() // Track only recipe-enabled items

    fun registerAll() {
        fun reg(item: CustomItem) {
            allItems.add(item)
            if (item is CustomItemWithRecipe) recipeItems.add(item)
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
    }
}
