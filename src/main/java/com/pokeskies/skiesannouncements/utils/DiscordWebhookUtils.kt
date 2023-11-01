package com.pokeskies.skiesannouncements.utils

import com.pokeskies.skiesannouncements.config.Announcement
import com.pokeskies.skiesannouncements.config.AnnouncementGroup
import com.pokeskies.skiesannouncements.config.discord.DiscordEmbed
import com.pokeskies.skiesannouncements.utils.Utils.mapInPlace
import java.io.IOException
import java.lang.reflect.Array
import java.net.URL
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

/**
 * Class used to execute Discord Webhooks with low effort. Adapted from https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 */

/**
 * Constructs a new DiscordWebhook instance
 *
 * @param url The webhook URL obtained in Discord
 */
class DiscordWebhookUtils(
    private val url: String,
    private var username: String = "null",
    private var avatarURL: String = "null",
    private var content: String? = null,
    private var embeds: MutableList<DiscordEmbed> = ArrayList(),
    private val group: AnnouncementGroup,
    private val announcement: Announcement
) {
    @Throws(IOException::class)
    fun execute() {
        if (content == null && embeds.isEmpty()) {
            throw IllegalArgumentException("Set content or add at least one EmbedObject")
        }

        val json = JSONObject()

        json.put("username", parsePlaceholders(username))
        json.put("avatar_url", avatarURL)
        json.put("content", if (content == null) null else parsePlaceholders(content!!))
        json.put("tts", false)

        // Handle embeds
        if (embeds.isNotEmpty()) {
            val embedObjects: MutableList<JSONObject> = ArrayList()
            for (embed: DiscordEmbed in embeds) {
                val jsonEmbed = JSONObject()

                if (embed.author != null) {
                    val jsonAuthor = JSONObject()
                    jsonAuthor.put("name", parsePlaceholders(embed.author.name))
                    jsonAuthor.put("url", embed.author.url)
                    jsonAuthor.put("icon_url", embed.author.iconURL)
                    jsonEmbed.put("author", jsonAuthor)
                }

                jsonEmbed.put("title", parsePlaceholders(embed.title))
                jsonEmbed.put("url", embed.url)
                jsonEmbed.put("description", parsePlaceholders(embed.description))

                val color = embed.getColor()
                if (color != null) {
                    var rgb = color.red
                    rgb = (rgb shl 8) + color.green
                    rgb = (rgb shl 8) + color.blue
                    jsonEmbed.put("color", rgb)
                }

                val jsonFields: MutableList<JSONObject> = ArrayList()
                for (field in embed.fields) {
                    val jsonField = JSONObject()
                    jsonField.put("name", parsePlaceholders(field.name))
                    jsonField.put("value", parsePlaceholders(field.value))
                    jsonField.put("inline", field.inline)
                    jsonFields.add(jsonField)
                }
                jsonEmbed.put("fields", jsonFields.toTypedArray())
                embedObjects.add(jsonEmbed)

                if (embed.thumbnail != null) {
                    val jsonThumbnail = JSONObject()
                    jsonThumbnail.put("url", embed.thumbnail.url)
                    jsonEmbed.put("thumbnail", jsonThumbnail)
                }

                if (embed.image != null) {
                    val jsonImage = JSONObject()
                    jsonImage.put("url", embed.image.url)
                    jsonEmbed.put("image", jsonImage)
                }

                if (embed.footer != null) {
                    val jsonFooter = JSONObject()
                    jsonFooter.put("text", parsePlaceholders(embed.footer.text))
                    jsonFooter.put("icon_url", embed.footer.iconURL)
                    jsonEmbed.put("footer", jsonFooter)
                }
            }
            json.put("embeds", embedObjects.toTypedArray())
        }
        val url = URL(url)
        val connection = url.openConnection() as HttpsURLConnection
        connection.addRequestProperty("Content-Type", "application/json")
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_")
        connection.setDoOutput(true)
        connection.setRequestMethod("POST")
        val stream = connection.outputStream
        stream.write(json.toString().toByteArray(StandardCharsets.UTF_8))
        stream.flush()
        stream.close()
        connection.inputStream.close() //I'm not sure why but it doesn't work without getting the InputStream
        connection.disconnect()
    }

    private inner class JSONObject {
        private val map = HashMap<String, Any>()
        fun put(key: String, value: Any?) {
            if (value != null) {
                map[key] = value
            }
        }

        override fun toString(): String {
            val builder = StringBuilder()
            val entrySet: Set<Map.Entry<String, Any>> = map.entries
            builder.append("{")
            var i = 0
            for ((key, `val`) in entrySet) {
                builder.append(quote(key)).append(":")
                if (`val` is String) {
                    builder.append(quote(`val`.toString()))
                } else if (`val` is Int) {
                    builder.append(`val`.toString().toInt())
                } else if (`val` is Boolean) {
                    builder.append(`val`)
                } else if (`val` is JSONObject) {
                    builder.append(`val`.toString())
                } else if (`val`.javaClass.isArray) {
                    builder.append("[")
                    val len = Array.getLength(`val`)
                    for (j in 0 until len) {
                        builder.append(Array.get(`val`, j).toString()).append(if (j != len - 1) "," else "")
                    }
                    builder.append("]")
                }
                builder.append(if (++i == entrySet.size) "}" else ",")
            }
            return builder.toString()
        }

        private fun quote(string: String): String {
            return "\"" + string + "\""
        }
    }

    private fun parsePlaceholders(string: String): String {
        var parsed = string

        if (string.contains("%message%")) {
            val message = announcement.createAnnouncementMessage(group).toMutableList()
            parsed = parsed.replace("%message%", message.joinToString("\\n"))
        }

        return parsed
    }
}