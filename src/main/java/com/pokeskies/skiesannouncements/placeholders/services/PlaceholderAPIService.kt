package com.pokeskies.skiesannouncements.placeholders.services

import com.pokeskies.skiesannouncements.placeholders.IPlaceholderService
import com.pokeskies.skiesannouncements.utils.Utils
import eu.pb4.placeholders.api.PlaceholderContext
import eu.pb4.placeholders.api.Placeholders
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class PlaceholderAPIService : IPlaceholderService {
    init {
        Utils.printInfo("PlaceholderAPI mod found! Enabling placeholder integration...")
    }
    override fun parsePlaceholders(player: ServerPlayerEntity, text: String): String {
        return Placeholders.parseText(Text.of(text), PlaceholderContext.of(player)).string
    }
}