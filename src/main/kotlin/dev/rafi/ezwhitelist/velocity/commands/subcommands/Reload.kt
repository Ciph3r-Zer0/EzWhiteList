package dev.rafi.ezwhitelist.velocity.commands.subcommands

import com.velocitypowered.api.command.CommandSource
import dev.rafi.ezwhitelist.common.helper.colorize
import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.MessageService

class Reload {
    companion object {
        fun reload(source: CommandSource, args: Array<String>) {
            if (!source.hasPermission("ezwhitelist.commands.reload")) {
                source.sendMessage(colorize(MessageService.NO_PERMISSION.replace("\$permission", "ezwhitelist.commands.reload")))
                return
            }

            if (ConfigService.reload() && MessageService.reload()) {
                source.sendMessage(colorize(MessageService.CONFIG_RELOAD_SUCCESS))
            } else {
                source.sendMessage(colorize(MessageService.CONFIG_RELOAD_FAILURE))
            }
        }
    }
}