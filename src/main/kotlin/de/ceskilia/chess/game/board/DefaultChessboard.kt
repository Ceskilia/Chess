package de.ceskilia.chess.game.board
import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.impl.*

class DefaultChessboard(game: ChessGame) : Chessboard(game) {

    override fun directionOf(color: ChessPiece.Color): Int {
        return when (color) {
            ChessPiece.Color.WHITE -> -1
            ChessPiece.Color.BLACK -> 1
        }
    }

    override fun setup() {
        board[0][4] = King(this, Position.of(0, 4), ChessPiece.Color.BLACK)
        board[0][3] = Queen(this, Position.of(0, 3), ChessPiece.Color.BLACK)
        board[0][1] = Knight(this, Position.of(0, 1), ChessPiece.Color.BLACK)
        board[0][6] = Knight(this, Position.of(0, 6), ChessPiece.Color.BLACK)
        board[0][2] = Bishop(this, Position.of(0, 2), ChessPiece.Color.BLACK)
        board[0][5] = Bishop(this, Position.of(0, 5), ChessPiece.Color.BLACK)
        board[0][0] = Rook(this, Position.of(0, 0), ChessPiece.Color.BLACK)
        board[0][7] = Rook(this, Position.of(0, 7), ChessPiece.Color.BLACK)

        for (y in 0 until DEFAULT_SIZE) {
            board[1][y] = Pawn(this, Position.of(1, y), ChessPiece.Color.BLACK)
        }

        //board[1][5] = Queen(this, Position.of(0, 3), ChessPiece.Color.BLACK)

        for (y in 0 until DEFAULT_SIZE) {
            board[6][y] = Pawn(this, Position.of(6, y), ChessPiece.Color.WHITE)
        }

        board[7][4] = King(this, Position.of(7, 4), ChessPiece.Color.WHITE)
        board[7][3] = Queen(this, Position.of(7, 3), ChessPiece.Color.WHITE)
        board[7][1] = Knight(this, Position.of(7, 1), ChessPiece.Color.WHITE)
        board[7][6] = Knight(this, Position.of(7, 6), ChessPiece.Color.WHITE)
        board[7][2] = Bishop(this, Position.of(7, 2), ChessPiece.Color.WHITE)
        board[7][5] = Bishop(this, Position.of(7, 5), ChessPiece.Color.WHITE)
        board[7][0] = Rook(this, Position.of(7, 0), ChessPiece.Color.WHITE)
        board[7][7] = Rook(this, Position.of(7, 7), ChessPiece.Color.WHITE)

        syncPieces()
    }

}