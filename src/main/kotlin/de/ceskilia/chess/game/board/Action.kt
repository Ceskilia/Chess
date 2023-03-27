package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece

data class Action(
    val position: Position,
    val action: (Position) -> Result
) {

    data class Result(val cancel: Boolean = false, val capturedPiece: ChessPiece? = null) {

        companion object {

            val DEFAULT: Result = Result()
            val CANCEL: Result = Result(cancel = true)

        }

    }

}
