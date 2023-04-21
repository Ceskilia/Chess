package de.ceskilia.chess.util

fun <T> MutableCollection<T>.addNonNull(element: T?): Boolean {
    return if (element != null) add(element) else false
}

fun <T> MutableCollection<T>.addElement(element: T): T {
    add(element)
    return element
}