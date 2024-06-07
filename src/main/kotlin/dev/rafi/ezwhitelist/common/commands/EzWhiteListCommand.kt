package dev.rafi.ezwhitelist.common.commands

import com.velocitypowered.api.command.CommandSource
import dev.rafi.ezwhitelist.common.helper.componentString
import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.MessageService
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute

@Command(name = "ezwhitelist", aliases = ["ewl", "ezwl"])
class EzWhiteListCommand {

    @Execute
    private fun main(@Context sender: CommandSource) {
        sender.sendMessage(componentString("<gray>[<blue><b>WhiteList<white>Plus</b><gray>] <obf>ii</obf> <gray>Version <white>1.0.0 <gray>by <white>Rafi <gray>(a.k.a <white>Cipher<gray>)"))
    }

    @Execute(name = "reload")
    private fun reload(@Context sender: CommandSource) {
        ConfigService.reload()
        MessageService.reload()
        // TODO: Send message
    }
}