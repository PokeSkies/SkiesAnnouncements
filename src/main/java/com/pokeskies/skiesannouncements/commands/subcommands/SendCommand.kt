package com.pokeskies.skiesannouncements.commands.subcommands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.config.ConfigManager
import com.pokeskies.skiesannouncements.utils.SubCommand
import com.pokeskies.skiesannouncements.utils.Utils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument

class SendCommand : SubCommand {
    override fun build(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("send")
            .requires(Permissions.require("skiesannouncements.command.send", 4))
            .then(Commands.argument("player", EntityArgument.players())
                .then(Commands.argument("group", StringArgumentType.string())
                    .suggests { _, builder ->
                        SharedSuggestionProvider.suggest(ConfigManager.GROUPS.keys.stream(), builder)
                    }
                    .then(
                        Commands.argument("id", StringArgumentType.string())
                            .suggests { ctx, builder ->
                                val groupId = StringArgumentType.getString(ctx, "group")
                                SharedSuggestionProvider.suggest(
                                    ConfigManager.GROUPS[groupId]?.announcements?.keys ?: emptyList(),
                                    builder
                                )
                            }
                            .executes(Companion::announceSpecific)
                    )
                    .executes(Companion::announce)
                )
            )
            .build()
    }

    companion object {
        fun announce(ctx: CommandContext<CommandSourceStack>): Int {
            val groupId = StringArgumentType.getString(ctx, "group")

            val group = ConfigManager.GROUPS[groupId]
            if (group == null) {
                ctx.source.sendMessage(
                    Utils.deserializeText("<red>Could not find an Announcement Group with the ID $groupId!")
                )
                return 1
            }

            val players = EntityArgument.getPlayers(ctx, "player")

            ctx.source.sendMessage(
                Utils.deserializeText("<green>Broadcasting an Announcement from the group $groupId to ${players.size} player(s)!")
            )
            group.broadcast(players.toList())

            return 1
        }

        fun announceSpecific(ctx: CommandContext<CommandSourceStack>): Int {
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

            val players = EntityArgument.getPlayers(ctx, "player")

            ctx.source.sendMessage(
                Utils.deserializeText("<green>Broadcasting the Announcement $announcementId from the group $groupId to ${players.size} player(s)!")
            )
            group.broadcast(announcement, players.toList())

            return 1
        }
    }
}
