package de.ceskilia.chess.game.piece.standardtype

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Action
import de.ceskilia.chess.game.piece.Promotable
import kotlin.math.abs

interface AbstractPawn : Promotable {

    val direction: Int
        get() = board.directionOf(this)

    override fun canPromote(): Boolean {
        return !board.inBounds(position.y + direction)
    }

    fun checkEnPassant(): Position? {
        val history = board.game.history
        val lastMove = history.lastMove() ?: return null
        val lastChessPiece = lastMove.chessPiece

        // if the last move somehow was done by the same color -> don't check for en passant
        if (lastChessPiece.color == color) {
            return null
        }

        // check for en passant
        val lastPosition = lastMove.endPosition
        val possibleEnPassant = (lastChessPiece is AbstractPawn)
                && (history.movesOf(lastChessPiece).size == 1)
                && (position.y == lastPosition.y)

        if (possibleEnPassant) {

            val xDifference = lastPosition.x - position.x

            // the pawns need to stand next to each other
            if (abs(xDifference) != 1) {
                return null
            }

            return position.copyAdding(xDifference, direction)
                .whenOccupied(result = Action.Result(capturedPiece = lastChessPiece)) {
                    board.removePieceAt(lastPosition)
                }
        }

        return null
    }

}