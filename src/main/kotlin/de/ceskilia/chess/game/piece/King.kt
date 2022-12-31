package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.Position
import de.ceskilia.chess.game.board.Chessboard

class King(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.KING) {

    var check: Boolean = false

    override fun calculateMoves(): Set<Position> {
        return emptySet()
    }

}