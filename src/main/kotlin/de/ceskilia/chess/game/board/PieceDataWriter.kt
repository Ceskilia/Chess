package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece

interface PieceDataWriter {

    fun placePiece(x: Int, y: Int, chessPiece: ChessPiece?)

    fun placePiece(position: Position, chessPiece: ChessPiece?) {
        placePiece(position.x, position.y, chessPiece)
    }

    fun placePieces(vararg pairs: Pair<Position, ChessPiece?>) {
        pairs.forEach {
            placePiece(it.first, it.second)
        }
    }

    fun removePieceAt(position: Position)

}