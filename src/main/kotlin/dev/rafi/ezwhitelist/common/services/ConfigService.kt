package dev.rafi.ezwhitelist.common.services

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import java.io.File
import java.nio.file.Path

class ConfigService(filePath: Path, fileName: String) {
    init {
        init(filePath, fileName)
    }

    companion object {
        lateinit var config: YamlDocument

        fun init(filePath: Path, fileName: String) {
            val inputStream = {}.javaClass.getResourceAsStream("/$fileName")
            if (inputStream == null) throw RuntimeException("$fileName is not bundled with the jar file")

            config = YamlDocument.create(
                File(filePath.toFile(), fileName),
                inputStream,
                GeneralSettings.DEFAULT,
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(BasicVersioning("config-version")).build()
            )

            initProperties()
        }

        fun reload() : Boolean {
            if (config.reload()) {
                initProperties()
                return true
            } else {
                return false
            }
        }

        lateinit var DB_TYPE: String
        lateinit var DB_HOST: String
        lateinit var DB_PORT: String
        lateinit var DB_NAME: String
        lateinit var DB_TABLE_NAME: String
        lateinit var DB_USERNAME: String
        lateinit var DB_PASSWORD: String

        var SAVE_INSTANT: Boolean = false
        var SAVE_INTERVAL: Long = 5

        var ADD_ON_CHECK: Boolean = true
        var ADD_INSTANT: Boolean = true
        var ADD_INTERVAL: Long = 5

        lateinit var CONFIG_VERSION: Number

        private fun initProperties() {
            DB_TYPE = config.getString("database.type")
            DB_HOST = config.getString("database.host")
            DB_PORT = config.getString("database.port")
            DB_NAME = config.getString("database.database_name")
            DB_TABLE_NAME = config.getString("database.table-name")
            DB_USERNAME = config.getString("database.user")
            DB_PASSWORD = config.getString("database.pass")

            SAVE_INSTANT = config.getBoolean("tasks.save.instant")
            SAVE_INTERVAL = config.getLong("tasks.save.interval")

            ADD_ON_CHECK = config.getBoolean("tasks.add.on-check")
            ADD_INSTANT = config.getBoolean("tasks.add.instant")
            ADD_INTERVAL = config.getLong("tasks.add.interval")

            CONFIG_VERSION = config.getInt("config-version")
        }
    }
}