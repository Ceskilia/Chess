package de.ceskilia.chess.util

fun notNegative(vararg numbers: Int, lazyMessage: () -> String) {
    for (number in numbers) {
        check(number >= 0, lazyMessage)
    }
}