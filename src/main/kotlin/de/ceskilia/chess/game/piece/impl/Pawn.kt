@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.DefaultChessPiece
import de.ceskilia.chess.game.piece.Promotable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateExtendedMovesPinned
import de.ceskilia.chess.util.whenOccupiedBy
import kotlin.math.abs

class Pawn(
    board: Chessboard,
    position: Position,
    color: ChessPiece.Color
) : DefaultChessPiece(board, position, color, ChessPiece.Type.PAWN), Promotable {

    override fun calculateMoves(): Set<Position> {
        return calculateExtendedMovesPinned(this)
    }

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()

        // check for normal moves
        val oneStep = position.tryCopyAdding(board.directionOf(color)) { x, y ->
            board.isBlankAt(x, y)
        }

        if (oneStep != null) {
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

        moves.addNonNull(checkNormalCaptures(-1))
        moves.addNonNull(checkNormalCaptures(1))

        // todo: what happens when oneStep not in board (promotion)

        val history = board.game.history
        val lastMove = history.lastMove() ?: return moves
        val lastChessPiece = lastMove.chessPiece

        // if the last move somehow was done by the same color -> don't check for en passant
        if (lastChessPiece.color == color) {
            return moves
        }

        // check for en passant
        val lastPosition = lastMove.newPosition
        val possibleEnPassant = lastChessPiece.type == ChessPiece.Type.PAWN
                && history.movesOf(lastChessPiece).size == 1
                && position.x == lastPosition.x

        if (possibleEnPassant) {

            val yDifference = lastPosition.y - position.y

            // the pawns need to stand next to each other
            if (abs(yDifference) != 1) {
                return moves
            }

            moves.add(position.copyAdding(board.directionOf(color), yDifference).whenOccupiedBy(this) {
                board.removePieceAt(lastPosition)
            })
        }

        return moves
    }

    override fun canPromote(): Boolean {
        return !board.isVerticalInBoard(position.x + board.directionOf(color))
    }

    override fun promote(): Creatable {
        val pieces = board.registeredCreatables
            .filter { it.color == this.color }

        while (true) {
            println("Please select a piece: ${pieces.map { it::class.simpleName }}")

            val input = readln().trim()
            val piece = pieces.firstOrNull { it::class.simpleName == input }

            if(piece != null) {
                return piece.createCopyAt(position)
            }

            println("This is not a valid piece!")
        }
    }

    private fun checkNormalCaptures(yDirection: Int): Position? {
        return position.tryCopyAdding(x = board.directionOf(color), y = yDirection) { x, y ->
            board.isOpponentAt(x, y, this)
        }
    }

}