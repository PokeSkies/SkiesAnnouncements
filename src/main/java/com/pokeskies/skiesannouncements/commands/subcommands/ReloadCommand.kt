package com.pokeskies.skiesannouncements.commands.subcommands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.SkiesAnnouncements
import com.pokeskies.skiesannouncements.utils.SubCommand
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class ReloadCommand : SubCommand {
    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("reload")
            .requires(Permissions.require("skiesannouncements.command.reload", 4))
            .executes(Companion::reload)
            .build()
    }

    companion object {
        fun reload(ctx: CommandContext<CommandSourceStack>): Int {
            SkiesAnnouncements.INSTANCE.reload()
            ctx.source.sendMessage(Utils.deserializeText("<green>Reloaded SkiesAnnouncements"))
            return 1
        }
    }
}
