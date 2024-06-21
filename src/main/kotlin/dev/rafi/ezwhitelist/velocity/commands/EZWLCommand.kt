package dev.rafi.ezwhitelist.velocity.commands

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.command.SimpleCommand.Invocation
import dev.rafi.ezwhitelist.common.helper.colorize
import dev.rafi.ezwhitelist.common.services.ServerService
import dev.rafi.ezwhitelist.velocity.EzWhiteListVelocity
import dev.rafi.ezwhitelist.velocity.commands.subcommands.*
import java.util.concurrent.CompletableFuture

class EZWLCommand : SimpleCommand {
    init {
        val cmdManager = EzWhiteListVelocity.proxyServer.commandManager
        val cmdMeta = cmdManager.metaBuilder("ezwhitelist").aliases("ewl", "ezwl", "whitelist", "wl").plugin(EzWhiteListVelocity.proxyServer).build()
        cmdManager.register(cmdMeta, this)
    }

    override fun execute(invocation: Invocation) {
        val source = invocation.source()
        val args = invocation.arguments()

        if (args.isEmpty()) {
            source.sendMessage(colorize("<gray>[<white><b>Ez<blue>WhiteList</b><gray>] <obf>ii</obf> <gray>Version <white>1.0.0 <gray>by <white>Rafi <gray>(a.k.a <white>Cipher<gray>)"))
            return
        }

        if (args[0].equals("On", true)) {
            On.on(source, args)
        } else if (args[0].equals("Off", true)) {
            Off.off(source, args)
        } else if (args[0].equals("Add", true)) {
            Add.add(source, args)
        } else if (args[0].equals("Remove", true)) {
            Remove.remove(source, args)
        } else if (args[0].equals("Reload", true)) {
            Reload.reload(source, args)
        }
    }

    override fun suggestAsync(invocation: Invocation): CompletableFuture<MutableList<String>> {
        val args = invocation.arguments()

        if (args.size <= 1) {
            return CompletableFuture.completedFuture(mutableListOf("on", "off", "add", "remove", "reload"))
        } else if (args.size == 2) {
            if (args[0].equals("on", true) || args[0].equals("off", true)) {
                return CompletableFuture.completedFuture(ServerService.serverList.filter { it.name != "__global__" }.map { it.name }.toMutableList())
            } else if (args[0].equals("add", true) || args[0].equals("remove", true)) {
                return CompletableFuture.completedFuture(EzWhiteListVelocity.proxyServer.allPlayers.map { it.username }.toMutableList())
            }
        } else if (args.size == 3) {
            return CompletableFuture.completedFuture(ServerService.serverList.filter { it.name != "__global__" }.map { it.name }.toMutableList())
        }

        return CompletableFuture.completedFuture(EzWhiteListVelocity.proxyServer.allPlayers.map { it.username }.toMutableList())
    }
}