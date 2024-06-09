package dev.rafi.ezwhitelist.common.services

import dev.dejvokep.boostedyaml.YamlDocument
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings
import dev.rafi.ezwhitelist.common.helper.colorizeComponent
import net.kyori.adventure.text.Component
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

        lateinit var CONFIG_RELOAD_SUCCESS: Component
        lateinit var CONFIG_RELOAD_FAILURE: Component
        lateinit var WHITELIST_ENABLE_GLOBAL: Component
        lateinit var WHITELIST_ENABLE_SERVER: Component
        lateinit var WHITELIST_DISABLE_GLOBAL: Component
        lateinit var WHITELIST_DISABLE_SERVER: Component
        lateinit var WHITELIST_ADD_GLOBAL: Component
        lateinit var WHITELIST_ADD_SERVER: Component
        lateinit var WHITELIST_REMOVE_GLOBAL: Component
        lateinit var WHITELIST_REMOVE_SERVER: Component

        lateinit var SERVER_NOT_FOUND: Component
        lateinit var WRONG_USAGE: Component

        lateinit var CONFIG_VERSION: Number

        private fun initProperties() {
            PREFIX = config.getString("prefix")

            CONFIG_RELOAD_SUCCESS = setPlaceHolders(config.getString("config-reload-success"))
            CONFIG_RELOAD_FAILURE = setPlaceHolders(config.getString("config-reload-failure"))
            WHITELIST_ENABLE_GLOBAL = setPlaceHolders(config.getString("whitelist-enable-global"))
            WHITELIST_ENABLE_SERVER = setPlaceHolders(config.getString("whitelist-enable-server"))
            WHITELIST_DISABLE_GLOBAL = setPlaceHolders(config.getString("whitelist-disable-global"))
            WHITELIST_DISABLE_SERVER = setPlaceHolders(config.getString("whitelist-disable-server"))
            WHITELIST_ADD_GLOBAL = setPlaceHolders(config.getString("whitelist-add-global"))
            WHITELIST_ADD_SERVER = setPlaceHolders(config.getString("whitelist-add-server"))
            WHITELIST_REMOVE_GLOBAL = setPlaceHolders(config.getString("whitelist-remove-global"))
            WHITELIST_REMOVE_SERVER = setPlaceHolders(config.getString("whitelist-remove-server"))

            SERVER_NOT_FOUND = setPlaceHolders(config.getString("server-not-found"))
            WRONG_USAGE = setPlaceHolders(config.getString("wrong-usage"))

            CONFIG_VERSION = config.getInt("config-version")
        }

        private fun setPlaceHolders(data: String): Component {
            return colorizeComponent(data.replace("{prefix}", PREFIX))
        }
    }
}