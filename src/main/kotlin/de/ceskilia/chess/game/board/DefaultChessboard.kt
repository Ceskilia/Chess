package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.Position
import de.ceskilia.chess.game.piece.Pawn

class DefaultChessboard : Chessboard() {

    override fun directionOf(color: ChessPiece.Color): Int {
        return when (color) {
            ChessPiece.Color.WHITE -> -1
            ChessPiece.Color.BLACK -> 1
        }
    }

    override fun setup() {

        for (y in 0 until DEFAULT_SIZE) {
            board[1][y] = Pawn(this, Position(1, y), ChessPiece.Color.BLACK)
        }

        for (y in 0 until DEFAULT_SIZE) {
            board[6][y] = Pawn(this, Position(6, y), ChessPiece.Color.WHITE)
        }

    }

}