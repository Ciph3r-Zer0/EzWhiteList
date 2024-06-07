package dev.rafi.ezwhitelist.common.helper

import dev.rafi.ezwhitelist.common.services.DBServer
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun getServer(serverName: String): ResultRow? {
    return transaction {
        DBServer.selectAll().where { DBServer.name eq serverName }.limit(1).firstOrNull()
    }
}

fun getStatus(resultRow: ResultRow): Boolean {
    return resultRow[DBServer.status]
}

fun getPlayerList(resultRow: ResultRow): List<String> {
    return resultRow[DBServer.players].split(",").toList()
}