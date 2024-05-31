package dev.rafi.whitelistplus.common

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Path
import java.sql.Connection

object DBServer : Table(ConfigService.DB_TABLE_NAME) {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", length = 32).uniqueIndex()
    val status: Column<Boolean> = bool("status").default(false).index()
    val players: Column<String> = text("players")

    override val primaryKey = PrimaryKey(id)
}

class DataBaseService(val filePath: Path) {
    init {
        initDataBase()
    }

    private fun initDataBase() {
        val dbType = ConfigService.DB_TYPE
        val sqLite = "SQLite"
        val mySql = "MySQL"

        if (dbType.equals(sqLite, true)) {
            sqLite()
        } else if (dbType.equals(mySql, true)) {
            mySql()
        } else {
            sqLite()
        }

        transaction {
            SchemaUtils.create(DBServer)
        }
    }

    private fun sqLite() {
        val sqLiteDriver = "org.sqlite.JDBC"
        val dbFile = File(filePath.toFile(), "${ConfigService.DB_NAME}.db")

        if (!(dbFile.exists())) dbFile.createNewFile()

        Database.connect("jdbc:sqlite:${dbFile.toPath()}", sqLiteDriver)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    private fun mySql() {
        val mySqlDriver = "com.mysql.cj.jdbc.Driver"

        Database.connect("jdbc:mysql://${ConfigService.DB_HOST}:${ConfigService.DB_PORT}/${ConfigService.DB_NAME}", driver = mySqlDriver, user = ConfigService.DB_USERNAME, password = ConfigService.DB_PASSWORD)
    }
}