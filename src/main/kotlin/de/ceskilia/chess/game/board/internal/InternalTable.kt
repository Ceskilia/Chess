package de.ceskilia.chess.game.board.internal

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.data.PieceDataHandler
import de.ceskilia.chess.piece.ChessPiece

class InternalTable(size: Int) : PieceDataHandler {

    val pieces: List<MutableList<ChessPiece?>>

    init {
        this.pieces = List(size) {
            MutableList(size) { null }
        }
    }

    override fun placePiece(chessPiece: ChessPiece?, x: Int, y: Int) {
        pieces[y][x] = chessPiece
        validate()
        chessPiece?.onMove()
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

    override fun positionOf(piece: ChessPiece): Position {
        for (y in pieces.indices) {
            for (x in pieces[y].indices) {
                val pieceAtIndex = pieceAt(x, y) ?: continue

                if (piece !== pieceAtIndex) {
                    continue
                }

                return Position.of(x, y)
            }
        }

        throw IllegalStateException("The provided piece is not placed on this board yet.")
    }

    override fun pieceAt(x: Int, y: Int): ChessPiece? {
        return if (inBounds(x, y)) pieces[y][x] else null
    }

    override fun inBounds(coordinate: Int): Boolean {
        return coordinate in pieces.indices
    }

    private fun validate() {
        val pieces = pieces.flatten()
            .filterNotNull()

        val occurrences = pieces.associateWith { piece ->
            pieces.count { piece === it }
        }

        occurrences.filterValues { it >= 2 }.forEach {
            throw IllegalStateException(
                "Piece ${it.key} can only be placed on one position, currently ${it.value}."
            )
        }
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