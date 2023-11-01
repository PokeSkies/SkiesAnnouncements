package com.pokeskies.skiesannouncements.commands.subcommands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.config.ConfigManager
import com.pokeskies.skiesannouncements.utils.SubCommand
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.command.CommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class AnnounceCommand : SubCommand {
    override fun build(): LiteralCommandNode<ServerCommandSource> {
        return CommandManager.literal("announce")
            .requires(Permissions.require("skiesannouncements.command.announce", 4))
            .then(CommandManager.argument("group", StringArgumentType.string())
                .suggests { _, builder ->
                    CommandSource.suggestMatching(ConfigManager.GROUPS.keys.stream(), builder)
                }
                .then(CommandManager.argument("id", StringArgumentType.string())
                    .suggests { ctx, builder ->
                        val groupId = StringArgumentType.getString(ctx, "group")
                        CommandSource.suggestMatching(
                            ConfigManager.GROUPS[groupId]?.announcements?.keys ?: emptyList(),
                            builder
                        )
                    }
                    .executes(Companion::announceSpecific)
                )
                .executes(Companion::announce)
            )
            .build()
    }

    companion object {
        fun announce(ctx: CommandContext<ServerCommandSource>): Int {
            val groupId = StringArgumentType.getString(ctx, "group")

            val group = ConfigManager.GROUPS[groupId]
            if (group == null) {
                ctx.source.sendMessage(
                    Utils.deserializeText("<red>Could not find an Announcement Group with the ID $groupId!")
                )
                return 1
            }

            ctx.source.sendMessage(
                Utils.deserializeText("<green>Broadcasting an Announcement from the group $groupId!")
            )

            group.broadcast()

            return 1
        }

        fun announceSpecific(ctx: CommandContext<ServerCommandSource>): Int {
            val groupId = StringArgumentType.getString(ctx, "group")

            val group = ConfigManager.GROUPS[groupId]
            if (group == null) {
                ctx.source.sendMessage(
                    Utils.deserializeText("<red>Could not find an Announcement Group with the ID $groupId!")
                )
                return 1
            }

            val announcementId = StringArgumentType.getString(ctx, "id")
            val announcement = group.announcements[announcementId]
            if (announcement == null) {
                ctx.source.sendMessage(
                    Utils.deserializeText("<red>Could not find an Announcement within Group $groupId with the ID $announcementId!")
                )
                return 1
            }

            ctx.source.sendMessage(
                Utils.deserializeText("<green>Broadcasting the Announcement $announcementId from the group $groupId!")
            )
            group.broadcast(announcement)

            return 1
        }
    }
}