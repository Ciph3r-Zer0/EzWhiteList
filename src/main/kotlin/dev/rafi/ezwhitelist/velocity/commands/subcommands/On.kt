package dev.rafi.ezwhitelist.velocity.commands.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.rafi.ezwhitelist.common.helper.colorize
import dev.rafi.ezwhitelist.common.services.MessageService
import dev.rafi.ezwhitelist.common.services.ServerService

class On {
    companion object {
        fun on(source: CommandSource, args: Array<String>) {
            if (!source.hasPermission("ezwhitelist.commands.on")) {
                source.sendMessage(colorize(MessageService.NO_PERMISSION.replace("\$permission", "ezwhitelist.commands.on")))
                return
            }
            val server = ServerService.getServerInstance(if (args.size > 1) args[1] else "__global__")

            if (server == null) {
                source.sendMessage(colorize(MessageService.SERVER_NOT_FOUND.replace("\$server", args[1])))
                return
            }

            server.status = true
            server.editCount++

            if (server.name == "__global__") {
                source.sendMessage(colorize(MessageService.WHITELIST_ENABLE_GLOBAL))
            } else {
                source.sendMessage(colorize(MessageService.WHITELIST_ENABLE_SERVER.replace("\$server", server.name)))
            }
        }
    }
}
