package dev.rafi.ezwhitelist.common.server

class Server(name: String) {
    var name = name
        private set
    var status: Boolean = false
    var players: MutableSet<String> = mutableSetOf()
}