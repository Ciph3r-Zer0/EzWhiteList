package dev.rafi.ezwhitelist.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import dev.rafi.ezwhitelist.common.services.*
import dev.rafi.ezwhitelist.velocity.commands.EZWLCommand
import dev.rafi.ezwhitelist.velocity.listeners.VelocityEvents
import dev.rafi.ezwhitelist.velocity.tasks.SaveTask
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
        var saveTask: ScheduledTask? = null

        fun start(plugin: EzWhiteListVelocity) {
            inst = plugin
            proxyServer = plugin.proxyServer
            logger = plugin.logger
            dataDir = plugin.dataDir

            ConfigService(dataDir, "config.yml")
            MessageService(dataDir, "messages.yml")
            EZWLCommand()
            VelocityEvents()

            DataBaseService(dataDir)

            transaction {
                DBServer.insertIgnore {
                    it[name] = "__global__"
                    it[players] = ""
                }
                proxyServer.allServers.forEach { server ->
                    DBServer.insertIgnore {
                        it[name] = server.serverInfo.name
                        it[players] = ""
                    }
                }
            }

            ServerService.loadAllServers()
            saveTask = SaveTask.startSaveTask(ConfigService.SAVE_INTERVAL)

            plugin.metricsFactory.make(plugin, 22181)
        }

        fun shutDown() {
            saveTask?.cancel()
        }
    }
}