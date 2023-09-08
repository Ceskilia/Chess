package de.ceskilia.chess.game.board.internal

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

data class Action(
    val position: Position,
    val result: Result,
    val action: (Position) -> Unit
) {

    data class Result(val cancel: Boolean = false, val capturedPiece: ChessPiece? = null) {

        companion object {

            val DEFAULT: Result = Result()
            val CANCEL: Result = Result(cancel = true)

        }

    }

}
