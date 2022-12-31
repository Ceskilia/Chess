package de.ceskilia.chess.game

import de.ceskilia.chess.util.isEven
import de.ceskilia.chess.util.notNegative

data class Position(val x: Int, val y: Int) {

    companion object {

        /**
         * The index where lowercase letters in the ASCII-table start.
         */
        private const val LETTER_START_POINT = 97

        // todo: add static factory method with caching (x=1, y=2 -> cache it and return if same args are provided)

        fun letterToCoordinate(number: Int): Char {
            return (number + LETTER_START_POINT).toChar()
        }

    }

    init {
        notNegative(x, y) { "Coordinates may be not negative: x=$x, y=$y" }
    }

    // use interface
    fun compareTo(other: Position): Distance {
        return Distance(other.x - this.x, other.y - this.y)
    }

    fun color(): Color {
        // the difference between x and y is always even
        return if ((x - y).isEven()) Color.WHITE else Color.BLACK
    }

    override fun toString(): String {
        return letterToCoordinate(y) + x.toString()
    }

    enum class Color {

        BLACK,
        WHITE

    }

}