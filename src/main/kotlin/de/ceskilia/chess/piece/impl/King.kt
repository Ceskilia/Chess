package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.util.addNonNull

class King(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), AbstractKing {

    override val notation: Char = 'K'

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

}