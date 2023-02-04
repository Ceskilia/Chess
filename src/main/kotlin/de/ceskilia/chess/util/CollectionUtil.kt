package de.ceskilia.chess.util

fun <T> MutableCollection<T>.addNonNull(element: T?): Boolean {
    return if(element != null) add(element) else false
}