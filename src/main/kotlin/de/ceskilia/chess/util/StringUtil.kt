package de.ceskilia.chess.util

import java.lang.StringBuilder

fun <T> StringBuilder.appendIf(value: T?, condition: (T) -> Boolean): StringBuilder {
    if(value != null && condition(value))
        append(value)
    return this
}