package com.pokeskies.skiesannouncements.config.discord

import com.google.gson.annotations.SerializedName
import com.pokeskies.skiesannouncements.utils.Utils
import java.awt.Color
import java.util.regex.Pattern

class DiscordEmbed(
    val author: Author? = null,
    val title: String = "",
    val url: String = "",
    val description: String = "",
    val color: String = "",
    val fields: List<Field> = emptyList(),
    val thumbnail: Thumbnail? = null,
    val image: Image? = null,
    val footer: Footer? = null
) {
    fun getColor(): Color? {
        if (color.isEmpty()) return null

        if (color.contains("#")) {
            if (Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$").matcher(color).matches()) {
                return Color(Integer.parseInt(color.replaceFirst("#", ""), 16))
            }
        } else {
            val split = color.split(",")
            val rgb = Array(split.size) { split[it].toInt() }
            if (rgb.size == 3) {
                return Color(rgb[0], rgb[1], rgb[2])
            }
        }

        Utils.printError("There was an error while processing a Discord Embed's color! The color '$color' was not in the format '#<HEX>' or '<0-255>,<0-255>,<0-255>'")
        return null
    }

    class Author(
        val name: String = "",
        val url: String = "",
        @SerializedName("icon_url")
        val iconURL: String = "",
    ) {
        override fun toString(): String {
            return "Author(name='$name', url='$url', iconURL='$iconURL')"
        }
    }

    class Field(
        val name: String = "",
        val value: String = "",
        val inline: Boolean = false
    ) {
        override fun toString(): String {
            return "Field(name='$name', value='$value', inline=$inline)"
        }
    }

    class Thumbnail(
        val url: String = "",
    ) {
        override fun toString(): String {
            return "Thumbnail(url='$url')"
        }
    }

    class Image(
        val url: String = "",
    ) {
        override fun toString(): String {
            return "Image(url='$url')"
        }
    }

    class Footer(
        val text: String = "",
        @SerializedName("icon_url")
        val iconURL: String = "",
    ) {
        override fun toString(): String {
            return "Footer(text='$text', iconURL='$iconURL')"
        }
    }

    override fun toString(): String {
        return "DiscordEmbed(author=$author, title='$title', url='$url', description='$description', color='$color', fields=$fields, thumbnail=$thumbnail, image=$image, footer=$footer)"
    }
}