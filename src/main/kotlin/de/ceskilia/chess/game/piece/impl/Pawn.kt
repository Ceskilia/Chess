@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateExtendedMovesPinned
import kotlin.math.abs

class Pawn(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.PAWN) {

    override fun calculateMoves(): Set<Position> {
        return calculateExtendedMovesPinned(this)
    }

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()

        // check for normal moves
        val oneStep = position.tryCopyAdding(board.directionOf(color)) { x, y ->
            board.isBlankAt(x, y)
        }

        if(oneStep != null) {
            moves.add(oneStep)

            val twoSteps = oneStep.tryCopyAdding(board.directionOf(color)) { x, y ->
                !hasMoved() && board.isBlankAt(x, y)
            }

            moves.addNonNull(twoSteps)
        }

        return moves
    }

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        moves.addNonNull(checkNormalCaptures(CaptureDirection.RIGHT))
        moves.addNonNull(checkNormalCaptures(CaptureDirection.LEFT))

        // todo: what happens when oneStep not in board (promotion)

        val lastMove = board.history.lastMove() ?: return moves

        // if the last move somehow was done by the same color -> don't check for en passant
        if (lastMove.chessPiece.color == color) {
            return moves
        }

        // check for en passant
        val lastPosition = lastMove.newPosition
        val possibleEnPassant = lastMove.chessPiece.type == Type.PAWN && position.x == lastPosition.x

        if (possibleEnPassant) {

            val yDifference = lastPosition.y - position.y

            // the pawns need to stand next to each other
            if (abs(yDifference) != 1) {
                return moves
            }

            board.removePieceAt(lastPosition)
            moves.add(position.copyAdding(board.directionOf(color), yDifference))
        }

        return moves
    }

    private fun checkNormalCaptures(direction: CaptureDirection): Position? {
        return position.tryCopyAdding(x = board.directionOf(color), y = direction.y) { x, y ->
            board.isOpponentAt(x, y, this)
        }
    }

    private enum class CaptureDirection(val y: Int) {

        LEFT(-1),
        RIGHT(1)

    }

}