package com.pokeskies.skiesannouncements.placeholders.services

import com.pokeskies.skiesannouncements.placeholders.IPlaceholderService
import net.minecraft.server.level.ServerPlayer

class DefaultPlaceholderService : IPlaceholderService {
    override fun parsePlaceholders(player: ServerPlayer, text: String): String {
        return text.replace("%player%", player.name.string)
    }
}
