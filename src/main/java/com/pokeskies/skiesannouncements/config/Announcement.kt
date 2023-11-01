package com.pokeskies.skiesannouncements.config

import com.google.gson.annotations.SerializedName
import com.pokeskies.skiesannouncements.utils.Utils
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import java.time.Duration

class Announcement(
    val message: List<String> = emptyList(),
    val title: TitleAnnouncement? = null,
    @SerializedName("action_bar")
    val actionBar: String = "",
    val sound: SoundAnnouncement? = null,
    val discord: Boolean = false,
) {
    fun sendAnnouncement(player: ServerPlayerEntity, group: AnnouncementGroup) {
        for (line in createAnnouncementMessage(player, group)) {
            player.sendMessage(line)
        }

        title?.sendTitle(player)

        if (actionBar.isNotEmpty()) {
            player.sendActionBar(Utils.deserializeText(Utils.parsePlaceholders(player, actionBar)))
        }

        sound?.playSound(player)
    }

    fun createAnnouncementMessage(player: ServerPlayerEntity, group: AnnouncementGroup): List<Component> {
        val announcement: MutableList<Component> = mutableListOf()
        if (group.formatting.isNotEmpty()) {
            for (formatLine in group.formatting) {
                if (formatLine.contains("%message%")) {
                    for (line in message) {
                        announcement.add(
                            Utils.deserializeText(
                                Utils.parsePlaceholders(player, formatLine.replace("%message%", line))
                            )
                        )
                    }
                } else {
                    announcement.add(Utils.deserializeText(Utils.parsePlaceholders(player, formatLine)))
                }
            }
        } else {
            for (line in message) {
                announcement.add(
                    Utils.deserializeText(
                        Utils.parsePlaceholders(player, line)
                    )
                )
            }
        }

        return announcement
    }

    fun createAnnouncementMessage(group: AnnouncementGroup): List<String> {
        val announcement: MutableList<String> = mutableListOf()
        if (group.formatting.isNotEmpty()) {
            for (formatLine in group.formatting) {
                if (formatLine.contains("%message%")) {
                    for (line in message) {
                        announcement.add(
                            Utils.stripText(
                                formatLine.replace("%message%", line)
                            )
                        )
                    }
                } else {
                    announcement.add(Utils.stripText(formatLine))
                }
            }
        } else {
            for (line in message) {
                announcement.add(
                    Utils.stripText(
                        line
                    )
                )
            }
        }

        return announcement
    }

    override fun toString(): String {
        return "Announcement(message=$message, title=$title, actionBar='$actionBar', sound=$sound, discord=$discord)"
    }

    class TitleAnnouncement(
        val title: String = "",
        val subtitle: String = "",
        val duration: Long = 5,
        val fadeIn: Long = 1,
        val fadeOut: Long = 1,
    ) {
        fun sendTitle(player: ServerPlayerEntity) {
            player.showTitle(
                Title.title(
                    Utils.deserializeText(Utils.parsePlaceholders(player, title)),
                    Utils.deserializeText(Utils.parsePlaceholders(player, subtitle)),
                    Title.Times.times(
                        Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut)
                    )
                )
            )
        }

        override fun toString(): String {
            return "Title(title='$title', subtitle='$subtitle', duration=$duration, fadeIn=$fadeIn, fadeOut=$fadeOut)"
        }
    }

    class SoundAnnouncement(
        val sound: SoundEvent? = null,
        val volume: Float = 1F,
        val pitch: Float = 1F,
    ) {
        fun playSound(player: ServerPlayerEntity) {
            if (sound == null) {
                Utils.printError("There was an error while executing a Sound Announcement for player ${player.name}: Sound was somehow null?")
                return
            }
            player.playSound(sound, SoundCategory.MASTER, volume, pitch)
        }

        override fun toString(): String {
            return "Sound(sound=$sound, volume=$volume, pitch=$pitch)"
        }

    }

}