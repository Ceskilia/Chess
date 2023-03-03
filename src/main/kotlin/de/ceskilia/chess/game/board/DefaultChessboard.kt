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

    private fun lastRankOf(color: ChessPiece.Color): Int {
        return when (color) {
            ChessPiece.Color.WHITE -> 7
            ChessPiece.Color.BLACK -> 0
        }
    }

    override fun setup() {

        for (color in ChessPiece.Color.values()) {
            val rank = lastRankOf(color)
            placePieces(
                King(this, color) to Position.of(4, rank),
                Queen(this, color) to Position.of(3, rank),
                Knight(this, color) to Position.of(1, rank),
                Knight(this, color) to Position.of(6, rank),
                Bishop(this, color) to Position.of(2, rank),
                Bishop(this, color) to Position.of(5, rank),
                Rook(this, color) to Position.of(0, rank),
                Rook(this, color) to Position.of(7, rank)
            )
        }

        for (x in 0 until size) {
            placePiece(Pawn(this, ChessPiece.Color.BLACK), x, 1)
            placePiece(Pawn(this, ChessPiece.Color.WHITE), x, 6)
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