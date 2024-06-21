package dev.rafi.ezwhitelist.common.services

import dev.rafi.ezwhitelist.common.server.Server
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ServerService {
    companion object {
        val serverList: MutableList<Server> = mutableListOf()

        fun createServer(name: String): Server {
            val server = Server(name)

            serverList.add(server)
            return server
        }

        fun getServerInstance(name: String): Server? {
            return serverList.find { it.name == name }
        }

        fun saveServer(server: Server) {
            transaction {
                DBServer.insert {
                    it[name] = server.name
                    it[status] = server.status
                    it[players] = server.players.joinToString(",")
                }
            }
        }

        fun upsertServer(server: Server) {
            transaction {
                val exists = DBServer.selectAll().where { DBServer.name eq server.name }.limit(1).firstOrNull()

                if (exists != null) {
                    DBServer.update({ DBServer.name eq server.name }) {
                        it[status] = server.status
                        it[players] = server.players.joinToString(",")
                    }
                } else {
                    DBServer.insert {
                        it[name] = server.name
                        it[status] = server.status
                        it[players] = server.players.joinToString(",")
                    }
                }
            }
        }

        fun removeServer(server: Server) {
            transaction {
                DBServer.deleteWhere { name eq server.name }
            }
            serverList.remove(server)
        }

        fun loadServer(resultRow: ResultRow) {
            val name = resultRow[DBServer.name]
            val status = resultRow[DBServer.status]
            val players = resultRow[DBServer.players].split(",").toMutableSet()

            val server = createServer(name)
            server.status = status
            server.players = players
        }

        fun loadAllServers() {
            transaction {
                DBServer.selectAll().forEach {
                    loadServer(it)
                }
            }
        }
    }
}