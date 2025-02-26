package com.pokeskies.skiesannouncements.config.requirements

import com.google.gson.*
import com.pokeskies.skiesannouncements.utils.Utils
import java.lang.reflect.Type

enum class ComparisonType(val identifier: String) {
    EQUALS("=="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUALS(">="),
    LESS_THAN_OR_EQUALS("<=");

    companion object {
        fun valueOfAnyCase(name: String): ComparisonType? {
            for (type in entries) {
                if (name.equals(type.identifier, true)) return type
            }
            return null
        }
    }

    internal class ComparisonTypeAdaptor : JsonSerializer<ComparisonType>, JsonDeserializer<ComparisonType> {
        override fun serialize(src: ComparisonType, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return JsonPrimitive(src.identifier)
        }

        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ComparisonType {
            val click = valueOfAnyCase(json.asString)

            if (click == null) {
                Utils.printError("Could not deserialize Comparison Type '${json.asString}'!")
                return EQUALS
            }

            return click
        }
    }
}
