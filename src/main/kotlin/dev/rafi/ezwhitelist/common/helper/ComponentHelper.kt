package dev.rafi.ezwhitelist.common.helper

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage


val miniMessage = MiniMessage.miniMessage()

fun colorize(input: String): Component {
    return miniMessage.deserialize(
        input
            .replace("&0", "<reset><black>")
            .replace("&1", "<reset><dark_blue>")
            .replace("&2", "<reset><dark_green>")
            .replace("&3", "<reset><dark_aqua>")
            .replace("&4", "<reset><dark_red>")
            .replace("&5", "<reset><dark_purple>")
            .replace("&6", "<reset><gold>")
            .replace("&7", "<reset><gray>")
            .replace("&8", "<reset><dark_gray>")
            .replace("&9", "<reset><blue>")

            .replace("&a", "<reset><green>")
            .replace("&b", "<reset><aqua>")
            .replace("&c", "<reset><red>")
            .replace("&d", "<reset><light_purple>")
            .replace("&e", "<reset><yellow>")
            .replace("&f", "<reset><white>")

            .replace("&l", "<bold>")
            .replace("&n", "<underlined>")
            .replace("&m", "<strikethrough>")
            .replace("&o", "<italic>")
            .replace("&r", "<reset>")

            .replace("&k", "<obfuscated>")
    )
}
