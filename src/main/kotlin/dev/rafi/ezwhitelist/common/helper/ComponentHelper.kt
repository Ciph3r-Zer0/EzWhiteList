package dev.rafi.ezwhitelist.common.helper

import dev.rafi.ezwhitelist.common.services.ConfigService
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


val miniMessage = MiniMessage.miniMessage()

fun colorizeAdventure(data: String): Component {
    return miniMessage.deserialize(data)
}

fun colorizeLegacy(data: String): Component {
    return Component.text(data.replace('&', '§'))
}

fun colorizeComponent(data: String): Component {
    if (ConfigService.LEGACY_COLORS) {
        return colorizeLegacy(data)
    } else {
        return colorizeAdventure(data)
    }
}

