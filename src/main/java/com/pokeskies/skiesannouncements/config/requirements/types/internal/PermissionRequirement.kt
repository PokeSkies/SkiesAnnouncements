package com.pokeskies.skiesannouncements.config.requirements.types.internal

import com.pokeskies.skiesannouncements.config.requirements.ComparisonType
import com.pokeskies.skiesannouncements.config.requirements.Requirement
import com.pokeskies.skiesannouncements.config.requirements.RequirementType
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.level.ServerPlayer

class PermissionRequirement(
    type: RequirementType = RequirementType.PERMISSION,
    comparison: ComparisonType = ComparisonType.EQUALS,
    private val permission: String = ""
) : Requirement(type, comparison) {
    override fun checkRequirements(player: ServerPlayer): Boolean {
        if (!checkComparison())
            return false

        Utils.printDebug("Checking a ${type?.identifier} Requirement with permission='$permission': $this")

        if (permission.isNotEmpty()) {
            val value = Permissions.check(player, permission)
            return if (comparison == ComparisonType.NOT_EQUALS) !value else value
        }

        return true
    }

    override fun allowedComparisons(): List<ComparisonType> {
        return listOf(ComparisonType.EQUALS, ComparisonType.NOT_EQUALS)
    }

    override fun toString(): String {
        return "PermissionRequirement(comparison=$comparison, permission='$permission')"
    }
}
