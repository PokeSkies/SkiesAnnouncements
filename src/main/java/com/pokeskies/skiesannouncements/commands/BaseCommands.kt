package com.pokeskies.skiesannouncements.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.LiteralCommandNode
import com.pokeskies.skiesannouncements.commands.subcommands.*
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

class BaseCommands {
    private val aliases = listOf("skiesannouncements", "announcements")

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val rootCommands: List<LiteralCommandNode<CommandSourceStack>> = aliases.map {
            Commands.literal(it).build()
        }

        val subCommands: List<LiteralCommandNode<CommandSourceStack>> = listOf(
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
