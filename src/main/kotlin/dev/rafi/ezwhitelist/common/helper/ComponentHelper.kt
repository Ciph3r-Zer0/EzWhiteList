package dev.rafi.ezwhitelist.common.helper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


val miniMessage = MiniMessage.miniMessage()

fun componentString(data: String): Component {
    return miniMessage.deserialize(data)
}

