package de.ceskilia.chess.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

fun <T> watchState(value: T): MutableState<T> {
    return mutableStateOf(value)
}

inline fun <T> watchState(value: () -> T): MutableState<T> {
    return watchState(value())
}

// this implementation design is somewhat evil, but we keep it for now
@Suppress("UNCHECKED_CAST")
fun <T : Any> stateOf(property: KProperty0<T>): MutableState<T> {
    val delegate = property.apply { isAccessible = true }.getDelegate()

    if (delegate is MutableState<*>) {
        if (delegate.value?.javaClass == property.get().javaClass) {
            return delegate as MutableState<T>
        }
    }

    return mutableStateOf(property.get())
}