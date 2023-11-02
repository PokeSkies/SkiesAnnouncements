package com.pokeskies.skiesannouncements.config.discord

import com.google.gson.annotations.SerializedName
import com.pokeskies.skiesannouncements.config.Announcement
import com.pokeskies.skiesannouncements.config.AnnouncementGroup
import com.pokeskies.skiesannouncements.utils.DiscordWebhookUtils
import com.pokeskies.skiesannouncements.utils.Utils
import java.io.IOException

class DiscordWebhook(
    @SerializedName("webhook_url")
    val webhookURL: String = "",
    val username: String = "",
    @SerializedName("avatar_url")
    val avatarURL: String = "",
    val content: String = "",
    val embeds: MutableList<DiscordEmbed> = mutableListOf(),
) {
    fun sendWebhook(group: AnnouncementGroup, announcement: Announcement) {
        if (webhookURL.isNotEmpty()) {
            val webhook = DiscordWebhookUtils(
                webhookURL,
                username,
                avatarURL,
                content,
                embeds,
                group,
                announcement
            )

            try {
                webhook.execute()
            } catch (e: IOException) {
                Utils.printError("Error while executing Discord webhook: ${e.printStackTrace()}")
            }
        }
    }

    override fun toString(): String {
        return "DiscordWebhook(webhookURL='$webhookURL', username='$username', avatarURL='$avatarURL', content='$content', embeds=$embeds)"
    }
}