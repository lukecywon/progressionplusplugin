package com.lukecywon.progressionPlus.items.armor.legendary

import com.lukecywon.progressionPlus.enums.Rarity
import com.lukecywon.progressionPlus.items.CustomItem
import com.lukecywon.progressionPlus.mechanics.ItemLore
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object TwilightCrown : CustomItem("twilight_crown", Rarity.LEGENDARY) {
    override fun createItemStack(): ItemStack {
        val item = createCustomHead("http://textures.minecraft.net/texture/407bae3fc5db207a88b6c102632f1e230945f7c4bf00aedbbd3d21adcc536f32")
        val meta = item.itemMeta

        meta.displayName(Component.text("Twilight Crown").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD))

        meta.lore(listOf(
            ItemLore.description("Its power resonates with ancient relics..."),
            ItemLore.description("Whispers stir when paired with the §dOld King's Blade§8."),
            ItemLore.separator(),
            ItemLore.lore("The crown once worn by a forgotten monarch."),
            ItemLore.lore("Those who listen closely may command what lies beyond.")
        ))

        meta.removeAttributeModifier(Attribute.ARMOR)
        meta.addAttributeModifier(
            Attribute.ARMOR,
            AttributeModifier(
                NamespacedKey(NamespacedKey.MINECRAFT, "armor"),
                1.0,
                AttributeModifier.Operation.ADD_NUMBER,
                EquipmentSlotGroup.HEAD
            )
        )

        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        item.itemMeta = meta

        return applyMeta(item)
    }

    override fun getExtraInfo(): List<String> {
        return listOf(
            "§7Found in End City Chests",
        )
    }
}

