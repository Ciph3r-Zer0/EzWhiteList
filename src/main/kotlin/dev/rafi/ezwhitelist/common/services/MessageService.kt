package dev.rafi.ezwhitelist.common.services

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import java.io.File
import java.nio.file.Path

class MessageService(filePath: Path, fileName: String) {
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

        lateinit var PREFIX: String

        lateinit var CONFIG_VERSION: Number

        private fun initProperties() {
            PREFIX = config.getString("prefix")

            CONFIG_VERSION = config.getInt("config-version")
        }
    }
}