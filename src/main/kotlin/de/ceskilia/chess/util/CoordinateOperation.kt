package de.ceskilia.chess.util

enum class CoordinateOperation(val operand: Int) {

    INCREMENT(1),
    DECREMENT(-1),
    CONSTANT(0);

    companion object {

        fun fromOperand(delta: Int): CoordinateOperation {
            return when {
                delta > 0 -> INCREMENT
                delta < 0 -> DECREMENT
                else -> CONSTANT
            }
        }

    }

}