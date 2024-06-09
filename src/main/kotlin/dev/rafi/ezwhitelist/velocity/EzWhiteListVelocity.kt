package dev.rafi.ezwhitelist.velocity

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.rafi.ezwhitelist.velocity.commands.EzWhiteListCommand
import dev.rafi.ezwhitelist.common.helper.miniMessage
import dev.rafi.ezwhitelist.common.services.*
import dev.rollczi.litecommands.LiteCommands
import dev.rollczi.litecommands.adventure.LiteAdventureExtension
import dev.rollczi.litecommands.velocity.LiteVelocityFactory
import org.bstats.velocity.Metrics
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "ezwhitelist",
    name = "EzWhiteList",
    version = "1.0.0",
    authors = ["Rafi"],
    description = "Your go to whitelist plugin"
)
class EzWhiteListVelocity @Inject constructor(
    private val proxyServer: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDir: Path,
    private val metricsFactory: Metrics.Factory
) {

    @Subscribe
    private fun proxyStartEvent(event: ProxyInitializeEvent) {
        start(this)
    }

    @Subscribe
    private fun proxyShutDownEvent(event: ProxyShutdownEvent) {
        shutDown()
    }

    companion object {
        lateinit var inst: EzWhiteListVelocity
        lateinit var proxyServer: ProxyServer
        lateinit var logger: Logger
        lateinit var dataDir: Path
        lateinit var liteCommands: LiteCommands<CommandSource>

        fun start(plugin: EzWhiteListVelocity) {
            inst = plugin
            proxyServer = plugin.proxyServer
            logger = plugin.logger
            dataDir = plugin.dataDir

            ConfigService(dataDir, "config.yml")
            MessageService(dataDir, "messages.yml")
            DataBaseService(dataDir)

            liteCommands = LiteVelocityFactory.builder(proxyServer)
                .extension(LiteAdventureExtension()) {
                    it.miniMessage(true)
                    it.legacyColor(true)
                    it.colorizeArgument(true)
                    it.serializer(miniMessage)
                }
                .commands(
                    EzWhiteListCommand()
                )
                .build()

            transaction {
                proxyServer.allServers.forEach { server ->
                    DBServer.insertIgnore {
                        it[name] = server.serverInfo.name
                        it[players] = ""
                    }
                }
            }

            ServerService.loadAllServers()

            ServerService.serverList.forEach {
                println("${it.name} | ${it.status}")
            }

            plugin.metricsFactory.make(plugin, 22181)
        }

        fun shutDown() {
            liteCommands.unregister()
        }
    }
}