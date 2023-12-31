package com.pokeskies.skiesannouncements.config.requirements

import com.google.gson.*
import com.pokeskies.skiesannouncements.config.requirements.types.internal.DimensionRequirement
import com.pokeskies.skiesannouncements.config.requirements.types.internal.PermissionRequirement
import java.lang.reflect.Type

enum class RequirementType(val identifier: String, val clazz: Class<*>) {
    PERMISSION("permission", PermissionRequirement::class.java),
    DIMENSION("dimension", DimensionRequirement::class.java);

    companion object {
        fun valueOfAnyCase(name: String): RequirementType? {
            for (type in values()) {
                if (name.equals(type.identifier, true)) return type
            }
            return null
        }
    }

    internal class RequirementTypeAdaptor : JsonSerializer<Requirement>, JsonDeserializer<Requirement> {
        override fun serialize(src: Requirement, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return context.serialize(src, src::class.java)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Requirement {
            val jsonObject: JsonObject = json.getAsJsonObject()
            val type: RequirementType? = valueOfAnyCase(jsonObject.get("type").asString)
            return try {
                context.deserialize(json, type!!.clazz)
            } catch (e: NullPointerException) {
                throw JsonParseException("Could not deserialize requirement type: $type", e)
            }
        }
    }
}