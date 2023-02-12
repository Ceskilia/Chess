package de.ceskilia.chess.game.result

class Draw(override val value: Type) : GameResult<Draw.Type> {

    enum class Type {

        STALEMATE,
        INSUFFICIENT_MATERIAL,
        THREEFOLD_REPETITION,
        FIFTY_MOVES

    }

}