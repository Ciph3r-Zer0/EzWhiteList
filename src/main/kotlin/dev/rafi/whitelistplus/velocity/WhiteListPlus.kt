package dev.rafi.whitelistplus.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.rafi.whitelistplus.common.ConfigService
import dev.rafi.whitelistplus.common.DataBaseService
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "whitelistplus",
    name = "WhiteListPlus",
    version = "1.0.0",
    authors = ["Rafi"],
    description = "Your go to whitelist plugin"
)
class WhiteListPlus @Inject constructor(
    private val proxyServer: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDir: Path,
    private val metricsFactory: Metrics.Factory
) {

    @Subscribe
    private fun proxyInitEvent(event: ProxyInitializeEvent) {
        init(this)

        ConfigService(dataDir, "config.yml")
        DataBaseService(dataDir)

        metricsFactory.make(this, 22092)
    }

    companion object {
        lateinit var proxyServer: ProxyServer
        lateinit var logger: Logger
        lateinit var dataDir: Path

        fun init(plugin: WhiteListPlus) {
            proxyServer = plugin.proxyServer
            logger = plugin.logger
            dataDir = plugin.dataDir
        }
    }
}