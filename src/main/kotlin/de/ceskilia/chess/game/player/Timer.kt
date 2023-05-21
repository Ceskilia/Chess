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

        const val DEFAULT_PREPARATION_TIME = 30

        private val SCHEDULED_TIMERS = mutableListOf<Timer>()

        init {
            fixedRateTimer(period = 1000, daemon = true) {
                SCHEDULED_TIMERS.removeIf { !it.hasTime() }
                SCHEDULED_TIMERS.filter(Timer::isRunning)
                    .forEach {
                        with(it) {

                            if (isPreparation) preparationTime--
                            else time--

                            if (preparationTime == 0) {
                                isPreparation = false
                            }

                            if (!hasTime()) {
                                pause()
                            }

                            action(it)
                        }
                    }
            }
        }

        fun of(
            initialTime: Int,
            initialPreparationTime: Int = DEFAULT_PREPARATION_TIME,
            action: (Timer) -> Unit
        ): Timer {
            notNegative(initialTime) { "Initial time may be not negative: value=$initialTime" }

            val timer = Timer(initialTime, initialPreparationTime, action)
            SCHEDULED_TIMERS.add(timer)
            return timer
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

    fun start() {
        if (!isPaused || !hasTime()) {
            return
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