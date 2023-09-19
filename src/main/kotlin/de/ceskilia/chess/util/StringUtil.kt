package de.ceskilia.chess.util

import java.lang.StringBuilder

fun StringBuilder.appendIf(value: Any?, condition: () -> Boolean): StringBuilder {
    if(value != null && condition())
        append(value)
    return this
}