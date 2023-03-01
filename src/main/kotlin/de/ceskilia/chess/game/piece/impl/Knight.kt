package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.util.addNonNull
import de.ceskilia.chess.util.isEven

class Knight(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color, ChessPiece.Type.KNIGHT), Creatable {

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        for(x in (-2..2).filter { it != 0 } ) {
            val y = if(x.isEven()) 1 else 2
            moves.addNonNull(position.tryCopyAdding(x, -y, board::isInBoard))
            moves.addNonNull(position.tryCopyAdding(x, y, board::isInBoard))
        }

        return moves
    }

    override fun createCopyAt(position: Position): Creatable {
        return Knight(this.board, this.color).withPosition(position)
    }

}