package com.pokeskies.skiesannouncements.utils

import com.google.gson.*
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.pokeskies.skiesannouncements.SkiesAnnouncements
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import java.lang.reflect.Type

object Utils {
    private val miniMessage: MiniMessage = MiniMessage.miniMessage()

    fun parsePlaceholders(player: ServerPlayer, text: String): String {
        return SkiesAnnouncements.INSTANCE.placeholderManager.parse(player, text)
    }

    fun deserializeText(text: String): Component {
        return miniMessage.deserialize(text)
    }

    fun stripText(text: String): String {
        return miniMessage.stripTags(text)
    }

    fun unEscapeString(s: String): String {
        val sb = java.lang.StringBuilder()
        for (i in s.indices) when (s[i]) {
            '\n' -> sb.append("\\n")
            '\t' -> sb.append("\\t")
            else -> sb.append(s[i])
        }
        return sb.toString()
    }

    fun printDebug(message: String, bypassCheck: Boolean = false) {
        if (bypassCheck || SkiesAnnouncements.INSTANCE.configManager.config.debug)
            SkiesAnnouncements.LOGGER.info("[SkiesAnnouncements] DEBUG: $message")
    }

    fun printError(message: String) {
        SkiesAnnouncements.LOGGER.error("[SkiesAnnouncements] ERROR: $message")
    }

    fun printInfo(message: String) {
        SkiesAnnouncements.LOGGER.info("[SkiesAnnouncements] $message")
    }

    inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
        val iterate = this.listIterator()
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            val newValue = mutator(oldValue)
            if (newValue !== oldValue) {
                iterate.set(newValue)
            }
        }
    }

    // Thank you to Patbox for these wonderful serializers =)
    data class RegistrySerializer<T>(val registry: Registry<T>) : JsonSerializer<T>, JsonDeserializer<T> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T? {
            var parsed = if (json.isJsonPrimitive) registry.get(ResourceLocation.parse(json.asString)) else null
            if (parsed == null)
                printError("There was an error while deserializing a Registry Type: $registry")
            return parsed
        }
        override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(registry.getId(src).toString())
        }
    }

    data class CodecSerializer<T>(val codec: Codec<T>) : JsonSerializer<T>, JsonDeserializer<T> {
        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): T? {
            return try {
                codec.decode(JsonOps.INSTANCE, json).orThrow.first
            } catch (e: Throwable) {
                printError("There was an error while deserializing a Codec: $codec")
                null
            }
        }

        override fun serialize(src: T?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            return try {
                if (src != null)
                    codec.encodeStart(JsonOps.INSTANCE, src).orThrow
                else
                    JsonNull.INSTANCE
            } catch (e: Throwable) {
                printError("There was an error while serializing a Codec: $codec")
                JsonNull.INSTANCE
            }
        }
    }
}
