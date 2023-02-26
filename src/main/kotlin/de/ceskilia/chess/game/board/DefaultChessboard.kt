package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.impl.*

class DefaultChessboard(game: ChessGame) : Chessboard(game) {

    override val registeredCreatables: List<Creatable> = registerCreatables()

    override fun directionOf(color: ChessPiece.Color): Int {
        return when (color) {
            ChessPiece.Color.WHITE -> -1
            ChessPiece.Color.BLACK -> 1
        }
    }

    override fun setup() {

        placePieces(
            Position.of(4, 0) to King(this, ChessPiece.Color.BLACK),
            Position.of(3, 0) to Queen(this, ChessPiece.Color.BLACK),
            Position.of(1, 0) to Knight(this, ChessPiece.Color.BLACK),
            Position.of(6, 0) to Knight(this, ChessPiece.Color.BLACK),
            Position.of(2, 0) to Bishop(this, ChessPiece.Color.BLACK),
            Position.of(5, 0) to Bishop(this, ChessPiece.Color.BLACK),
            Position.of(0, 0) to Rook(this, ChessPiece.Color.BLACK),
            Position.of(7, 0) to Rook(this, ChessPiece.Color.BLACK),
            //
            Position.of(4, 7) to King(this, ChessPiece.Color.WHITE),
            Position.of(3, 7) to Queen(this, ChessPiece.Color.WHITE),
            Position.of(1, 7) to Knight(this, ChessPiece.Color.WHITE),
            Position.of(6, 7) to Knight(this, ChessPiece.Color.WHITE),
            Position.of(2, 7) to Bishop(this, ChessPiece.Color.WHITE),
            Position.of(5, 7) to Bishop(this, ChessPiece.Color.WHITE),
            Position.of(0, 7) to Rook(this, ChessPiece.Color.WHITE),
            Position.of(7, 7) to Rook(this, ChessPiece.Color.WHITE)
        )

        for (x in 0 until size) {
            placePiece(x, 1, Pawn(this, ChessPiece.Color.BLACK))
            placePiece(x, 6, Pawn(this, ChessPiece.Color.WHITE))
        }

        syncPieces()
    }

    private fun registerCreatables(): List<Creatable> {
        val creatables = mutableListOf<Creatable>()

        for (color in ChessPiece.Color.values()) {
            creatables.add(Queen(this, color))
            creatables.add(Rook(this, color))
            creatables.add(Knight(this, color))
            creatables.add(Bishop(this, color))
        }

        return creatables
    }

}