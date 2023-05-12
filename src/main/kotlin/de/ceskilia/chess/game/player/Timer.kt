@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.player

import de.ceskilia.chess.util.notNegative
import kotlin.concurrent.fixedRateTimer

class Timer(val initialTime: Int, val initialPreparationTime: Int = DEFAULT_PREPARATION_TIME) {

    // TODO: OPTIMISATION - CODEFLOW AND || NAMING ||

    companion object {

        const val DEFAULT_PREPARATION_TIME = 30

        private val TIMERS: List<Timer> = mutableListOf()

        init {
            fixedRateTimer(period = 60 * 1000) {
                TIMERS.forEach {

                    if (it.isPreparation) it.preparationTime--
                    else it.time--

                }
            }
        }

        fun of(initialTime: Int): Timer {
            return Timer(initialTime)
        }

    }

    init {
        notNegative(initialTime) { "Initial time may be not negative: value=$initialTime" }
    }

    var hasStarted: Boolean = false
        private set
    var isPaused: Boolean = true
        private set
    var isPreparation: Boolean = false
        private set

    var time: Int = initialTime
        private set
    var preparationTime: Int = initialPreparationTime
        private set

    fun start() {
        if (!isPaused) {
            return
        }

        isPaused = false

        if (!hasStarted) {
            isPreparation = true
            return
        }
    }

    fun pause() {
        if (isPaused) {
            return
        }

        isPaused = true

        if (!hasStarted) {
            hasStarted = true
            isPreparation = false
            return
        }

    }

}