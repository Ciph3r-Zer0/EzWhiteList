package dev.rafi.ezwhitelist.common.server

import dev.rafi.ezwhitelist.common.services.ConfigService
import dev.rafi.ezwhitelist.common.services.ServerService

class Server(name: String) {
    val name = name
    var status: Boolean = false
    var players: MutableSet<String> = mutableSetOf()

    var editCount = 0
        set(value) {
            field = value
            if (ConfigService.SAVE_INSTANT) {
                ServerService.upsertServer(this)
                field = 0
            }
        }
}