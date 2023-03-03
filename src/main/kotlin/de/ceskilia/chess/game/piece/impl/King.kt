package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.Checkable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves
import de.ceskilia.chess.util.whenOccupiedBy

class King(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color, ChessPiece.Type.KING), Checkable {

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves()
            .plus(checkCastle())
            .toMutableSet()

        moves.removeIf {
            val isAlly = board.isAllyAt(it, this)
            val isCovered = it.isCoveredByOpponent()

            isAlly || isCovered
        }

        return moves
    }

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        for (x in -1..1) {
            for (y in -1..1) {

                // this is the current position, skip it
                if (x == 0 && y == 0) {
                    continue
                }

                moves.addNonNull(position.tryCopyAdding(x, y, board::isInBoard))
            }
        }

        return moves
    }

    private fun checkCastle(): Set<Position> {

        if (hasMoved() || isChecked()) {
            return emptySet()
        }

        return setOfNotNull(
            castle(Operation.INCREMENT), // short-castle
            castle(Operation.DECREMENT) // long-castle
        )
    }

    private fun castle(xOperation: Operation): Position? {
        val line = calculateCoveringArithmeticMoves(xOperation)
            .mapNotNull { board.pieceAt(it) }
            .filter { it.type == ChessPiece.Type.ROOK }

        if (line.size != 1) {
            return null
        }

        val rook = line.first()

        if (rook.hasMoved()) {
            return null
        }

        val direction = xOperation.operand
        val newRookPosition = this.position.copyAdding(x = direction)
        val endPosition = this.position.copyAdding(x = 2 * direction)
        val blocked = newRookPosition.isCoveredByOpponent() || endPosition.isCoveredByOpponent()

        if (blocked) {
            return null
        }

        return endPosition.whenOccupiedBy(this) {
            board.moveUnchecked(rook, newRookPosition)
        }
    }

}