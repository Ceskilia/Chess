@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.Promotable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateExtendedMovesPinned
import kotlin.math.abs

class Pawn(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color, ChessPiece.Type.PAWN), Promotable {

    private val direction = board.directionOf(color)

    override fun calculateMoves(): Set<Position> {
        return calculateExtendedMovesPinned()
    }

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves()
            .filter { board.isOpponentAt(it, this) }
            .toMutableSet()
        moves.addNonNull(checkEnPassant())

        // check for normal moves
        val oneStep = position.tryCopyAdding(y = direction) { x, y ->
            board.isBlankAt(x, y)
        }

        if (oneStep != null) {
            moves.add(oneStep)

            val twoSteps = oneStep.tryCopyAdding(y = direction) { x, y ->
                !hasMoved() && board.isBlankAt(x, y)
            }

            moves.addNonNull(twoSteps)
        }

        return moves
    }

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()
        moves.addNonNull(checkCapture(1))
        moves.addNonNull(checkCapture(-1))
        return moves
    }

    override fun canPromote(): Boolean {
        return !board.inBounds(position.y + direction)
    }

    override fun promote(): Creatable {
        val pieces = board.registeredCreatables
            .filter { it.color == this.color }

        while (true) {
            println("Please select a piece: ${pieces.map { it::class.simpleName }}")

            val input = readln().trim()
            val piece = pieces.firstOrNull { it::class.simpleName == input }

            if (piece != null) {
                return piece.createCopyAt(position)
            }

            println("This is not a valid piece!")
        }
    }

    private fun checkCapture(xDirection: Int): Position? {
        return position.tryCopyAdding(x = xDirection, y = direction, board::inBounds)
    }

    private fun checkEnPassant(): Position? {
        val history = board.game.history
        val lastMove = history.lastMove() ?: return null
        val lastChessPiece = lastMove.chessPiece

        // if the last move somehow was done by the same color -> don't check for en passant
        if (lastChessPiece.color == color) {
            return null
        }

        // check for en passant
        val lastPosition = lastMove.endPosition
        val possibleEnPassant = lastChessPiece.type == ChessPiece.Type.PAWN
                && history.movesOf(lastChessPiece).size == 1
                && position.y == lastPosition.y

        if (possibleEnPassant) {

            val xDifference = lastPosition.x - position.x

            // the pawns need to stand next to each other
            if (abs(xDifference) != 1) {
                return null
            }

            return position.copyAdding(xDifference, direction).whenOccupied {
                board.removePieceAt(lastPosition)
            }
        }

        return null
    }

}