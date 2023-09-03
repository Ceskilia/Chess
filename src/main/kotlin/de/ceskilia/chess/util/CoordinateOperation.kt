package de.ceskilia.chess.util

enum class CoordinateOperation(val direction: Int) {

    INCREMENT(1),
    DECREMENT(-1),
    CONSTANT(0);

    companion object {

        fun fromDirection(direction: Int): CoordinateOperation {
            return when {
                direction > 0 -> INCREMENT
                direction < 0 -> DECREMENT
                else -> CONSTANT
            }
        }

    }

}