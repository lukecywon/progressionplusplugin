package com.lukecywon.progressionPlus.utils

import com.destroystokyo.paper.profile.PlayerProfile
import com.lukecywon.progressionPlus.ProgressionPlus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.profile.PlayerTextures
import java.net.URL
import java.util.*

object HeadMaker {
    fun createCustomHead(url: String): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        val validUrl = URL(url)

        // Create empty profile with random UUID
        val profile: PlayerProfile = Bukkit.createPlayerProfile(UUID.randomUUID()) as PlayerProfile

        // Set the base64 texture
        val textures: PlayerTextures = profile.textures
        textures.skin = validUrl
        profile.setTextures(textures)

        // Apply the profile to the item
        meta.playerProfile = profile

        // Make head unplaceable
        meta.persistentDataContainer.set(
            NamespacedKey(ProgressionPlus.getPlugin(), "wearable_head"),
            PersistentDataType.BYTE,
            1
        )

        head.itemMeta = meta

        return head
    }

    fun isCustomHead(item: ItemStack): Boolean {
        val meta = item.itemMeta ?: return false

        return meta.persistentDataContainer.has(
            NamespacedKey(ProgressionPlus.getPlugin(), "wearable_head"),
            PersistentDataType.BYTE
        )
    }
}