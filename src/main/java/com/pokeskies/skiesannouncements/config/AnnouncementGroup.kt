package com.pokeskies.skiesannouncements.config

import com.pokeskies.skiesannouncements.SkiesAnnouncements
import com.pokeskies.skiesannouncements.config.requirements.Requirement
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

class AnnouncementGroup(
    val enabled: Boolean = true,
    val interval: Int = 300,
    val order: AnnouncementOrder = AnnouncementOrder.RANDOM,
    val formatting: List<String> = emptyList(),
    val requirements: Map<String, Requirement> = emptyMap(),
    val discord: DiscordMessage = DiscordMessage(),
    val announcements: HashMap<String, Announcement> = hashMapOf()
) {
    var remainingTime = -1

    companion object {
        var LAST_ANNOUNCED: Int = -1
    }

    fun broadcast() {
        broadcast(getAnnouncement())
    }

    fun broadcast(players: List<ServerPlayerEntity>) {
        broadcast(getAnnouncement(), players)
    }

    fun broadcast(announcement: Announcement) {
        broadcast(announcement, SkiesAnnouncements.INSTANCE.server?.playerManager?.playerList ?: emptyList())
    }

    fun broadcast(announcement: Announcement, players: List<ServerPlayerEntity>) {
        for (player in players) {
            if (checkRequirements(player)) {
                announcement.sendAnnouncement(player, this)
            }
        }
    }

    private fun getAnnouncement(): Announcement {
        return when (order) {
            AnnouncementOrder.SEQUENTIAL -> {
                LAST_ANNOUNCED = ++LAST_ANNOUNCED % announcements.size
                announcements.values.toTypedArray()[LAST_ANNOUNCED]
            }
            AnnouncementOrder.RANDOM -> {
                announcements.values.toTypedArray()[Random().nextInt(announcements.size)]
            }
        }
    }

    private fun checkRequirements(player: ServerPlayerEntity): Boolean {
        for (requirement in requirements) {
            if (!requirement.value.checkRequirements(player))
                return false
        }
        return true
    }

    override fun toString(): String {
        return "AnnouncementGroup(enabled=$enabled, interval=$interval, order=$order, formatting=$formatting, requirements=$requirements, discord=$discord, announcements=$announcements)"
    }
}