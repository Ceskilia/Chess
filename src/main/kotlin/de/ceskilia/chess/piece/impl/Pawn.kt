package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.board.internal.Action
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Promotable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateExtendedMovesPinned
import kotlin.math.abs

@Valuable(1)
class Pawn(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), AbstractPawn {

    // notation can change dynamically
    override val notation: Char
        get() = Position.coordinateToLetter(this.position.x)

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

    override fun promote(): Promotable.Context {
        val creatables = board.registeredCreatables
            .filter { it.color == this.color }

        while (true) {
            println("Please select a piece: ${creatables.map { it::class.simpleName }}")

            val creatableTypeName = readln().trim()
            val creatable = board.promotePiece(
                color = this.color,
                typeName = creatableTypeName
            )

            if (creatable != null) {
                return Promotable.Context(creatable, this.position)
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
                .whenOccupying(result = Action.Result(capturedPiece = lastChessPiece)) {
                    board.removePieceAt(lastPosition)
                }
        }

        return null
    }

}