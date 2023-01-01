package de.ceskilia.chess.game.arithmetic

import de.ceskilia.chess.util.isEven
import de.ceskilia.chess.util.notNegative

class Position private constructor(val x: Int, val y: Int) {

    companion object {

        // see https://youtrack.jetbrains.com/issue/KT-11914/Confusing-data-class-copy-with-private-constructor

        /**
         * A cache for positions. Positions are immutable and only holding values, so it's safe to save them.
         */
        private val CACHED_POSITIONS = mutableSetOf<Position>()

        /**
         * The index where lowercase letters in the ASCII-table start.
         */
        private const val LETTER_START_POINT = 97

        fun of(x: Int, y: Int): Position {
            notNegative(x, y) { "Coordinates may be not negative: x=$x, y=$y" }
            return CACHED_POSITIONS.firstOrNull { it.x == x && it.y == y } ?: cachePosition(Position(x, y))
        }

        fun letterToCoordinate(number: Int): Char {
            return (number + LETTER_START_POINT).toChar()
        }

        private fun cachePosition(position: Position): Position {
            CACHED_POSITIONS.add(position)
            return position
        }

    }

    // use interface
    fun compareTo(other: Position): Distance {
        return Distance(other.x - this.x, other.y - this.y)
    }

    fun color(): Color {
        // the difference between x and y is always even
        return if ((x - y).isEven()) Color.WHITE else Color.BLACK
    }

    fun copy(x: Int = this.x, y: Int = this.y): Position = of(x, y)

    fun copyAdding(x: Int = 0, y: Int = 0): Position {
        return of(this.x + x, this.y + y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return letterToCoordinate(y) + x.toString()
    }

    enum class Color {

        BLACK,
        WHITE

    }

}