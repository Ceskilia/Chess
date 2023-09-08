package de.ceskilia.chess.game.board.data

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

interface PieceDataWriter {

    fun placePiece(chessPiece: ChessPiece?, x: Int, y: Int)

    fun placePiece(chessPiece: ChessPiece?, position: Position) {
        placePiece(chessPiece, position.x, position.y)
    }

    fun placePieces(vararg pairs: Pair<ChessPiece?, Position>) {
        pairs.forEach {
            placePiece(it.first, it.second)
        }
    }

    fun moveUnchecked(piece: ChessPiece, position: Position) {
        removePieceAt(piece.position)
        placePiece(piece, position)
    }

    fun removePiece(piece: ChessPiece) {
        removePieceAt(piece.position)
    }

    fun removePieceAt(position: Position) {
        placePiece(null, position)
    }

}