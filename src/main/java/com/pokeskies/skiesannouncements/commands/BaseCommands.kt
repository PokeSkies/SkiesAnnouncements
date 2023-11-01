package com.pokeskies.skiesannouncements.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.commands.subcommands.*
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class BaseCommands {
    private val aliases = listOf("skiesannouncements", "announcements")

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        val rootCommands: List<LiteralCommandNode<ServerCommandSource>> = aliases.map {
            CommandManager.literal(it).build()
        }

        val subCommands: List<LiteralCommandNode<ServerCommandSource>> = listOf(
            ReloadCommand().build(),
            DebugCommand().build(),
            ListCommand().build(),
            AnnounceCommand().build(),
            SendCommand().build(),
        )

        rootCommands.forEach { root ->
            subCommands.forEach { sub -> root.addChild(sub) }
            dispatcher.root.addChild(root)
        }
    }
}