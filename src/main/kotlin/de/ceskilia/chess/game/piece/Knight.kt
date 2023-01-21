package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.isEven

class Knight(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.KNIGHT) {

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()

        for(x in (-2..2).filter { it != 0 } ) {
            val y = if(x.isEven()) 1 else 2
            moves.add(position.copyAdding(x, -y))
            moves.add(position.copyAdding(x, y))
        }

        return moves
    }

}