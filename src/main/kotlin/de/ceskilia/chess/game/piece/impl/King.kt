package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece

class King(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.KING) {

    override fun calculateCoveringMoves(): Set<Position> {
        return modifyMovesIf(mutableSetOf(), Operation.ADD) { !board.isAllyAt(it, this) }
    }

    override fun calculateMoves(): Set<Position> {
        return modifyMovesIf(calculateCoveringMoves().toMutableSet(), Operation.REMOVE) { position ->
            board.getOpponentPieces(color).any { it.isCovering(position) }
        }
    }

    private fun modifyMovesIf(
        moves: MutableSet<Position>,
        operation: Operation,
        condition: (Position) -> Boolean
    ): Set<Position> {

        for (x in -1..1) {
            for (y in -1..1) {

                // this is the current position, skip it
                if (x == 0 && y == 0) {
                    continue
                }

                val position = position.tryCopyAdding(x, y, board::isValidPosition) ?: continue

                if (!condition(position)) {
                    continue
                }

                when (operation) {
                    Operation.ADD -> moves.add(position)
                    Operation.REMOVE -> moves.remove(position)
                }

            }
        }

        return moves
    }

    private enum class Operation {

        ADD,
        REMOVE

    }

}