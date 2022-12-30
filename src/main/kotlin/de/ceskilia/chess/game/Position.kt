package de.ceskilia.chess.game

data class Position(val x: Int, val y: Int) {

    companion object {

        /**
         * The index where lowercase letters in the ASCII-table start.
         */
        private const val LETTER_START_POINT = 97

        fun letterToCoordinate(number: Int): Char {
            return (number + LETTER_START_POINT).toChar()
        }

    }

    // use interface
    fun compareTo(other: Position): Distance {
        return Distance(other.x - this.x, other.y - this.y)
    }

    override fun toString(): String {
        return letterToCoordinate(y) + x.toString()
    }

}