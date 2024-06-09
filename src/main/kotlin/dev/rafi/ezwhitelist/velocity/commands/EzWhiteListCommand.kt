package dev.rafi.ezwhitelist.velocity.commands

import com.velocitypowered.api.command.CommandSource
import dev.rafi.ezwhitelist.common.helper.colorizeAdventure
import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.MessageService
import dev.rafi.ezwhitelist.common.services.ServerService
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.context.Context
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.optional.OptionalArg
import net.kyori.adventure.text.TextReplacementConfig

@Command(name = "ezwhitelist", aliases = ["ewl", "ezwl", "whitelist", "wl"])
class EzWhiteListCommand {

    @Execute
    private fun main(@Context sender: CommandSource) {
        sender.sendMessage(colorizeAdventure("<gray>[<white><b>Ez<blue>WhiteList</b><gray>] <obf>ii</obf> <gray>Version <white>1.0.0 <gray>by <white>Rafi <gray>(a.k.a <white>Cipher<gray>)"))
    }

    /*
    Commands:
        - On (serverName)   [DONE]
        - Off (serverName)   [DONE]
        - Add <playerName> (serverName)   [DONE]
        - Remove <playerName> (serverName)   [DONE]
        - Status (serverName)
        - List (serverName) (Page)
        - Help
        - Reload    [DONE]
     */

    @Execute(name = "on")
    private fun on(@Context sender: CommandSource, @OptionalArg serverName: String?) {
        if (serverName == null) {
            ServerService.serverList.forEach {
                it.status = true
            }
            sender.sendMessage(MessageService.WHITELIST_ENABLE_GLOBAL)
        } else {
            val server = ServerService.getServerInstance(serverName)
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{server}").replacement(serverName)
                .build()

            if (server == null) {
                sender.sendMessage(MessageService.SERVER_NOT_FOUND.replaceText(placeHolders))
                return
            }
            server.status = true
            sender.sendMessage(MessageService.WHITELIST_ENABLE_SERVER.replaceText(placeHolders))
        }
    }

    @Execute(name = "off")
    private fun off(@Context sender: CommandSource, @OptionalArg serverName: String?) {
        if (serverName == null) {
            ServerService.serverList.forEach {
                it.status = false
            }
            sender.sendMessage(MessageService.WHITELIST_DISABLE_GLOBAL)
        } else {
            val server = ServerService.getServerInstance(serverName)
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{server}").replacement(serverName)
                .build()

            if (server == null) {
                sender.sendMessage(MessageService.SERVER_NOT_FOUND.replaceText(placeHolders))
                return
            }
            server.status = false
            sender.sendMessage(MessageService.WHITELIST_DISABLE_SERVER.replaceText(placeHolders))
        }
    }

    @Execute(name = "add")
    private fun add(@Context sender: CommandSource, @OptionalArg playerName: String, @OptionalArg serverName: String?) {
        if (serverName == null) {
            ServerService.serverList.forEach {
                it.players.add(playerName)
            }
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{player}").replacement(playerName)
                .build()
            sender.sendMessage(MessageService.WHITELIST_ADD_GLOBAL.replaceText(placeHolders))

        } else {
            val server = ServerService.getServerInstance(serverName)
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{player}").replacement(playerName)
                .match("\\{server}").replacement(serverName)
                .build()

            if (server == null) {
                sender.sendMessage(MessageService.SERVER_NOT_FOUND.replaceText(placeHolders))
                return
            }
            server.players.add(playerName)
            sender.sendMessage(MessageService.WHITELIST_ADD_SERVER.replaceText(placeHolders))
        }
    }

    @Execute(name = "add")
    private fun remove(@Context sender: CommandSource, @OptionalArg playerName: String, @OptionalArg serverName: String?) {
        if (serverName == null) {
            ServerService.serverList.forEach {
                it.players.remove(playerName)
            }
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{player}").replacement(playerName)
                .build()
            sender.sendMessage(MessageService.WHITELIST_REMOVE_GLOBAL.replaceText(placeHolders))
        } else {
            val server = ServerService.getServerInstance(serverName)
            val placeHolders = TextReplacementConfig.builder()
                .match("\\{player}").replacement(playerName)
                .match("\\{server}").replacement(serverName)
                .build()

            if (server == null) {
                sender.sendMessage(MessageService.SERVER_NOT_FOUND.replaceText(placeHolders))
                return
            }
            server.players.remove(playerName)
            sender.sendMessage(MessageService.WHITELIST_REMOVE_SERVER.replaceText(placeHolders))
        }
    }

    @Execute(name = "reload", aliases = ["r"])
    private fun reload(@Context sender: CommandSource) {
        if (ConfigService.reload() && MessageService.reload()) {
            sender.sendMessage(MessageService.CONFIG_RELOAD_SUCCESS)
        } else {
            sender.sendMessage(MessageService.CONFIG_RELOAD_FAILURE)
        }
    }
}