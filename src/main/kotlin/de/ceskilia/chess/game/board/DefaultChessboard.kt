package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.Bishop
import de.ceskilia.chess.game.piece.King
import de.ceskilia.chess.game.piece.Pawn

class DefaultChessboard : Chessboard() {

    override fun directionOf(color: ChessPiece.Color): Int {
        return when (color) {
            ChessPiece.Color.WHITE -> -1
            ChessPiece.Color.BLACK -> 1
        }
    }

    override fun setup() {

        board[0][4] = King(this, Position.of(0, 4), ChessPiece.Color.BLACK)

        for (y in 0 until DEFAULT_SIZE) {
            board[1][y] = Pawn(this, Position.of(1, y), ChessPiece.Color.BLACK)
        }

        board[7][4] = King(this, Position.of(7, 4), ChessPiece.Color.WHITE)

        for (y in 0 until DEFAULT_SIZE) {
            board[6][y] = Pawn(this, Position.of(6, y), ChessPiece.Color.WHITE)
        }

        board[4][4] = Bishop(this, Position.of(4, 4), ChessPiece.Color.WHITE)

        syncPieces()
    }

}