package com.pokeskies.skiesannouncements.placeholders.services

import com.pokeskies.skiesannouncements.placeholders.IPlaceholderService
import com.pokeskies.skiesannouncements.utils.Utils
import eu.pb4.placeholders.api.PlaceholderContext
import eu.pb4.placeholders.api.Placeholders
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

class PlaceholderAPIService : IPlaceholderService {
    init {
        Utils.printInfo("PlaceholderAPI mod found! Enabling placeholder integration...")
    }
    override fun parsePlaceholders(player: ServerPlayer, text: String): String {
        return Placeholders.parseText(Component.literal(text), PlaceholderContext.of(player)).string
    }
}
