package com.pokeskies.skiesannouncements.utils

import com.mojang.brigadier.tree.LiteralCommandNode
import net.minecraft.server.command.ServerCommandSource

interface SubCommand {
    fun build(): LiteralCommandNode<ServerCommandSource>
}