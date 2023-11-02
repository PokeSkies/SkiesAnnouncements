package com.pokeskies.skiesannouncements.config.requirements.types.internal

import com.pokeskies.skiesannouncements.config.requirements.ComparisonType
import com.pokeskies.skiesannouncements.config.requirements.Requirement
import com.pokeskies.skiesannouncements.config.requirements.RequirementType
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.network.ServerPlayerEntity

class DimensionRequirement(
    type: RequirementType = RequirementType.DIMENSION,
    comparison: ComparisonType = ComparisonType.EQUALS,
    private val id: String = ""
) : Requirement(type, comparison) {
    override fun checkRequirements(player: ServerPlayerEntity): Boolean {
        if (!checkComparison())
            return false

        Utils.printDebug("Checking a ${type?.identifier} Requirement with id='$id': $this")

        val value = id.equals(player.world.registryKey.value.toString(), true)
        return if (comparison == ComparisonType.NOT_EQUALS) !value else value
    }

    override fun allowedComparisons(): List<ComparisonType> {
        return listOf(ComparisonType.EQUALS, ComparisonType.NOT_EQUALS)
    }

    override fun toString(): String {
        return "DimensionRequirement(comparison=$comparison, id='$id')"
    }
}