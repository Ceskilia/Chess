package de.ceskilia.chess.game.result

class Draw(val type: Type) : GameResult {

    enum class Type {

        AGREEMENT,
        STALEMATE,
        INSUFFICIENT_MATERIAL,
        INSUFFICIENT_MATERIAL_TIMEOUT,
        THREEFOLD_REPETITION,
        FIFTY_MOVES

    }

}