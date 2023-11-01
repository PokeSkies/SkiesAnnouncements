package com.pokeskies.skiesannouncements.config

import com.google.gson.*
import com.pokeskies.skiesannouncements.utils.Utils
import java.lang.reflect.Type

enum class AnnouncementOrder(val identifier: String) {
    SEQUENTIAL("sequential"),
    RANDOM("random");

    companion object {
        fun valueOfAnyCase(name: String): AnnouncementOrder? {
            for (type in values()) {
                if (name.equals(type.identifier, true)) return type
            }
            return null
        }
    }

    internal class Adaptor : JsonSerializer<AnnouncementOrder>, JsonDeserializer<AnnouncementOrder> {
        override fun serialize(src: AnnouncementOrder, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src.identifier)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AnnouncementOrder {
            val order = valueOfAnyCase(json.asString)

            if (order == null) {
                Utils.printError("Could not deserialize Announcement Order '${json.asString}'! Falling back to RANDOM")
                return RANDOM
            }

            return order
        }
    }
}