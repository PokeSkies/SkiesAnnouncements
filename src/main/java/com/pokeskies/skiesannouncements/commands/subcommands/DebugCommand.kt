package com.pokeskies.skiesannouncements.commands.subcommands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.SkiesAnnouncements
import com.pokeskies.skiesannouncements.utils.SubCommand
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class DebugCommand : SubCommand {
    override fun build(): LiteralCommandNode<ServerCommandSource> {
        return CommandManager.literal("debug")
            .requires(Permissions.require("skiesannouncements.command.debug", 4))
            .executes(Companion::debug)
            .build()
    }

    companion object {
        fun debug(ctx: CommandContext<ServerCommandSource>): Int {
            val newMode = !SkiesAnnouncements.INSTANCE.configManager.config.debug
            SkiesAnnouncements.INSTANCE.configManager.config.debug = newMode
            SkiesAnnouncements.INSTANCE.configManager.saveFile("config.json", SkiesAnnouncements.INSTANCE.configManager.config)

            if (newMode) {
                ctx.source.sendMessage(Utils.deserializeText("<green>Debug mode has been enabled!"))
            } else {
                ctx.source.sendMessage(Utils.deserializeText("<red>Debug mode has been disabled!"))
            }
            return 1
        }
    }
}