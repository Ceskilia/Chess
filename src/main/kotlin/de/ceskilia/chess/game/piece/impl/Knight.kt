package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.annotation.Valuable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.isEven

@Valuable(3u)
class Knight(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), Creatable {

    override val notation: Char = 'N'

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        for(x in (-2..2).filter { it != 0 } ) {
            val y = if(x.isEven()) 1 else 2
            moves.addNonNull(position.tryCopyAdding(x, -y, board::inBounds))
            moves.addNonNull(position.tryCopyAdding(x, y, board::inBounds))
        }

        return moves
    }

    override fun createCopyAt(position: Position): Creatable {
        return Knight(this.board, this.color).withPosition(position)
    }

}