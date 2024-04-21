package com.pokeskies.skiesannouncements.commands.subcommands

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.SkiesAnnouncements
import com.pokeskies.skiesannouncements.config.ConfigManager
import com.pokeskies.skiesannouncements.utils.SubCommand
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class ReloadCommand : SubCommand {
    override fun build(): LiteralCommandNode<ServerCommandSource> {
        return CommandManager.literal("reload")
            .requires(Permissions.require("skiesannouncements.command.reload", 4))
            .executes(Companion::reload)
            .build()
    }

    companion object {
        fun reload(ctx: CommandContext<ServerCommandSource>): Int {
            SkiesAnnouncements.INSTANCE.reload()
            ctx.source.sendMessage(Utils.deserializeText("<green>Reloaded SkiesAnnouncements"))
            Utils.printDebug("Loaded Config: ", true)
            Utils.printDebug("${SkiesAnnouncements.INSTANCE.configManager.config}", true)
            Utils.printDebug("Loaded Announcements:", true)
            for ((id, announcement) in ConfigManager.GROUPS) {
                Utils.printDebug("- $id: $announcement", true)
            }
            return 1
        }
    }
}