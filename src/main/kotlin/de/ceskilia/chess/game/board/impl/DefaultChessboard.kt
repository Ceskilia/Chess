package de.ceskilia.chess.game.board.impl

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.impl.*
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Creatable

class DefaultChessboard(game: ChessGame) : Chessboard(game) {

    override val registeredCreatables: List<Creatable> = registerCreatables()

    override fun directionOf(pawn: AbstractPawn): Int {
        return directionOf(pawn.color)
    }

    private fun directionOf(color: ChessPiece.Color): Int {
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
            val lastRank = lastRankOf(color)

            placePieces(
                King(this, color) to Position.of(4, lastRank),
                Queen(this, color) to Position.of(3, lastRank),
                Knight(this, color) to Position.of(1, lastRank),
                Knight(this, color) to Position.of(6, lastRank),
                Bishop(this, color) to Position.of(2, lastRank),
                Bishop(this, color) to Position.of(5, lastRank),
                Rook(this, color) to Position.of(0, lastRank),
                Rook(this, color) to Position.of(7, lastRank)
            )

            for (x in 0 until size) {
                placePiece(Pawn(this, color), x, lastRank + directionOf(color))
            }

        }

        validate()
        syncPieces()
    }

    private fun validate() {
        val kings = pieces().filterIsInstance<AbstractKing>()

        check(kings.size == 2) { "There must be exactly 2 kings on the board." }
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