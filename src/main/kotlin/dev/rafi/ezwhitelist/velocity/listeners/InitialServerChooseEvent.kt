package dev.rafi.ezwhitelist.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import dev.rafi.ezwhitelist.common.helper.getPlayerList
import dev.rafi.ezwhitelist.common.helper.getServer
import dev.rafi.ezwhitelist.common.helper.getStatus
import dev.rafi.ezwhitelist.velocity.EzWhiteListVelocity

class InitialServerChooseEvent {
    init {
        EzWhiteListVelocity.proxyServer.eventManager.register(EzWhiteListVelocity.inst, this)
    }

    @Subscribe
    fun chooseInitialServerEvent(event: PlayerChooseInitialServerEvent) {
        if (event.initialServer.isEmpty) return

        val player = event.player
        val dbServer = getServer(event.initialServer.get().serverInfo.name) ?: return
        val isWhitelist = getStatus(dbServer)

        if (!isWhitelist) return
        val players = getPlayerList(dbServer)

        if (players.contains(player.username)) return

        // TODO
//        player.disconnect("server whitelisted")
    }
}