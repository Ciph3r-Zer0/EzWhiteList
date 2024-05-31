package dev.rafi.whitelistplus

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
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
    @DataDirectory private val dataDir: Path
) {

    @Subscribe
    private fun proxyInitEvent(event: ProxyInitializeEvent) {
        
    }
}