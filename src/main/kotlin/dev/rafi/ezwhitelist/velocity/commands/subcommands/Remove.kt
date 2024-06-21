package dev.rafi.ezwhitelist.velocity.commands.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.rafi.ezwhitelist.common.helper.colorize
import dev.rafi.ezwhitelist.common.services.MessageService
import dev.rafi.ezwhitelist.common.services.ServerService

class Remove {
    companion object {
        fun remove(source: CommandSource, args: Array<String>) {
            if (!source.hasPermission("ezwhitelist.commands.remove")) {
                source.sendMessage(colorize(MessageService.NO_PERMISSION.replace("\$permission", "ezwhitelist.commands.remove")))
                return
            }
            if (args.size == 1) {
                source.sendMessage(colorize(MessageService.WRONG_USAGE))
                return
            }
            val playerName = args[1]
            val server = ServerService.getServerInstance(if (args.size > 2) args[2] else "__global__")

            if (server == null) {
                source.sendMessage(colorize(MessageService.SERVER_NOT_FOUND.replace("\$server", args[2])))
                return
            }

            if (!(server.players.contains(playerName))) {
                source.sendMessage(
                    colorize(
                    if (server.name == "__global__") {
                        MessageService.PLAYER_NOT_EXISTS_GLOBAL.replace("\$player", playerName)
                    } else {
                        MessageService.PLAYER_NOT_EXISTS_SERVER.replace("\$player", playerName).replace("\$server", server.name)
                    }
                )
                )
            } else {
                server.players.remove(playerName)
                server.editCount++

                if (server.name == "__global__") {
                    source.sendMessage(colorize(MessageService.WHITELIST_REMOVE_GLOBAL.replace("\$player", playerName)))
                } else {
                    source.sendMessage(colorize(MessageService.WHITELIST_REMOVE_SERVER.replace("\$player", playerName).replace("\$server", server.name)))
                }
            }
        }
    }
}