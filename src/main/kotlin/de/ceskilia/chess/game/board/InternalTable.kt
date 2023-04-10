@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.piece.ChessPiece
import java.util.*

class InternalTable(size: Int) : PieceDataHandler {

    val pieces: List<MutableList<ChessPiece?>>

    init {
        this.pieces = List(size) {
            MutableList(size) { null }
        }
    }

    override fun placePiece(chessPiece: ChessPiece?, x: Int, y: Int) {
        pieces[y][x] = chessPiece
    }

    override fun pieces(color: ChessPiece.Color?): List<ChessPiece> {
        return pieces.flatten()
            .filterNotNull()
            .filter { color == null || it.color == color }
    }

    override fun opponentPieces(color: ChessPiece.Color): List<ChessPiece> {
        return pieces.flatten()
            .filterNotNull()
            .filter { it.color != color }
    }

    override fun pieceAt(x: Int, y: Int): ChessPiece? {
        return if (inBounds(x, y)) pieces[y][x] else null
    }

    override fun inBounds(coordinate: Int): Boolean {
        return coordinate in pieces.indices
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InternalTable

        if (pieces != other.pieces) return false

        return true
    }

    override fun hashCode(): Int {
        return pieces.hashCode()
    }

}