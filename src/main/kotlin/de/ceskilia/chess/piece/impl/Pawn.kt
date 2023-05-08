package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateExtendedMovesPinned

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

}