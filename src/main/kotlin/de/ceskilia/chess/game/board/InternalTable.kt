@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece

class InternalTable : PieceDataHandler {

    val pieces: List<MutableList<ChessPiece?>>

    constructor(size: Int) {
        this.pieces = List(size) {
            MutableList(size) { null }
        }
    }

    private constructor(pieces: List<MutableList<ChessPiece?>>) {
        this.pieces = pieces
    }

    fun copy(): InternalTable {
        return InternalTable(this.pieces)
    }

    override fun placePiece(chessPiece: ChessPiece?, x: Int, y: Int) {
        pieces[y][x] = chessPiece
    }

    override fun removePieceAt(position: Position) {
        pieces[position.y][position.x] = null
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
        return if (isInBoard(x, y)) pieces[y][x] else null
    }

    override fun isInBoard(coordinate: Int): Boolean {
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