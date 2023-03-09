package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.Checkable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

class King(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color, ChessPiece.Type.KING), Checkable {

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()

        moves.removeIf {
            val isAlly = board.isAllyAt(it, this)
            val isCovered = it.isCoveredByOpponent()

            isAlly || isCovered
        }

        return moves.plus(checkCastle())
    }

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        for (x in -1..1) {
            for (y in -1..1) {

                // this is the current position, skip it
                if (x == 0 && y == 0) {
                    continue
                }

                moves.addNonNull(position.tryCopyAdding(x, y, board::inBounds))
            }
        }

        return moves
    }

    private fun checkCastle(): Set<Position> {

        if (hasMoved() || isChecked()) {
            return emptySet()
        }

        return checkCastleDirection(Operation.INCREMENT) + checkCastleDirection(Operation.DECREMENT)
    }

    private fun checkCastleDirection(xOperation: Operation): Set<Position> {
        val line = calculateCoveringArithmeticMoves(xOperation)
            .mapNotNull { board.pieceAt(it) }
            .filter { it.type == ChessPiece.Type.ROOK }

        if (line.size != 1) {
            return emptySet()
        }

        val rook = line.first()

        if (rook.hasMoved()) {
            return emptySet()
        }

        val direction = xOperation.operand
        val newRookPosition = this.position.copyAdding(x = direction)
        val endPosition = this.position.copyAdding(x = 2 * direction)
        val blocked = newRookPosition.isCoveredByOpponent() || endPosition.isCoveredByOpponent()

        if (blocked) {
            return emptySet()
        }

        val rookMove = rook.position.whenOccupied(true) {
            board.moveUnchecked(rook, newRookPosition)
            board.moveUnchecked(this, endPosition)
        }
        val twoSteps = endPosition.whenOccupied {
            board.moveUnchecked(rook, newRookPosition)
        }

        return setOf(rookMove, twoSteps)
    }

}