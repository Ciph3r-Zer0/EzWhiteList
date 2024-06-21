package dev.rafi.ezwhitelist.velocity.listeners

import com.velocitypowered.api.event.ResultedEvent
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import dev.rafi.ezwhitelist.common.helper.colorize
import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.MessageService
import dev.rafi.ezwhitelist.common.services.ServerService
import dev.rafi.ezwhitelist.velocity.EzWhiteListVelocity

class VelocityEvents {
    init {
        EzWhiteListVelocity.proxyServer.eventManager.register(EzWhiteListVelocity.inst, this)
    }

    @Subscribe
    private fun onLogin(event: LoginEvent) {
        val globalServer = ServerService.getServerInstance("__global__")

        if (globalServer == null) {
            ServerService.upsertServer(ServerService.createServer("__global__"))
            return
        }

        if (!(globalServer.status)) return

        val playerName = event.player.username

        if (!(globalServer.players.contains(playerName))) {
            event.result = ResultedEvent.ComponentResult.denied(colorize(MessageService.KICK))
        }
    }

    @Subscribe
    private fun onInitialServerChoose(event: PlayerChooseInitialServerEvent) {
        val player = event.player
        val server = event.initialServer

        if (server.isEmpty) return

        val globalServer = ServerService.getServerInstance("__global__")
        val cachedServer = ServerService.getServerInstance(server.get().serverInfo.name)

        if (globalServer == null) {
            ServerService.createServer("__global__")
        }
        if (cachedServer == null) {
            if (ConfigService.ADD_ON_CHECK) {
                ServerService.createServer(server.get().serverInfo.name)
            }
            return
        }

        if ((globalServer?.status == true || cachedServer.status) && (globalServer?.players?.contains(player.username) == false && !cachedServer.players.contains(player.username))) {
            player.disconnect(colorize(MessageService.KICK))
        }
    }

    @Subscribe
    private fun onServerSwitch(event: ServerPreConnectEvent) {
        val player = event.player
        val server = event.originalServer
        val globalServer = ServerService.getServerInstance("__global__")
        val cachedServer = ServerService.getServerInstance(server.serverInfo.name)

        if (globalServer == null) {
            ServerService.createServer("__global__")
        }
        if (cachedServer == null) {
            if (ConfigService.ADD_ON_CHECK) {
                ServerService.createServer(server.serverInfo.name)
            }
            return
        }

        if ((globalServer?.status == true || cachedServer.status) && (globalServer?.players?.contains(player.username) == false && !cachedServer.players.contains(player.username))) {
            event.result = ServerPreConnectEvent.ServerResult.denied()
            player.sendMessage(colorize(MessageService.PREVENT_SWITCH))
        }
    }
}