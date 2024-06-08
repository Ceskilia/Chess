@file:Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")

package de.ceskilia.chess.game.player

import de.ceskilia.chess.util.notNegative
import kotlin.concurrent.fixedRateTimer

class Timer private constructor(
    val initialTime: Int,
    val initialPreparationTime: Int,
    val action: (Timer) -> Unit
) {

    companion object {

        // todo: maybe shutdown pool?

        const val DEFAULT_PREPARATION_TIME = 30

        fun of(
            initialTime: Int,
            initialPreparationTime: Int = DEFAULT_PREPARATION_TIME,
            action: (Timer) -> Unit
        ): Timer {
            notNegative(initialTime) { "Initial time may be not negative: value=$initialTime" }

            return Timer(initialTime, initialPreparationTime, action)
        }

    }

    var isPreparation: Boolean = true
        private set
    var isPaused: Boolean = true
        private set
    val isRunning: Boolean
        get() = !isPaused

    var time: Int = initialTime
        private set
    var preparationTime: Int = initialPreparationTime
        private set

    private var isScheduling: Boolean = false

    fun initScheduling() {
        if (isScheduling) {
            return
        }

        isScheduling = true
        fixedRateTimer(period = 1000, daemon = true) {

            if (isPaused) {
                return@fixedRateTimer
            }

            if (isPreparation) preparationTime--
            else time--

            if (preparationTime == 0) {
                isPreparation = false
            }

            if (!hasTime()) {
                pause()
                cancel()
                isScheduling = false
            }

            action(this@Timer)
        }
    }

    fun start() {
        if (!isPaused || !hasTime()) {
            return
        }

        if (!isScheduling) {
            initScheduling()
        }

        isPaused = false
    }

    fun pause() {
        if (isPaused) {
            return
        }

        isPaused = true

        if (isPreparation) {
            isPreparation = false
        }

    }

    fun hasTime(): Boolean {
        return time != 0
    }

}