package de.ceskilia.chess.game.board.data

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

interface PieceDataReader {

    fun pieces(color: ChessPiece.Color? = null): List<ChessPiece>

    fun opponentPieces(color: ChessPiece.Color): List<ChessPiece>

    fun positionOf(piece: ChessPiece): Position

    fun pieceAt(x: Int, y: Int): ChessPiece?

    fun pieceAt(position: Position): ChessPiece? {
        return pieceAt(position.x, position.y)
    }

    fun isPieceAt(x: Int, y: Int): Boolean {
        return pieceAt(x, y) != null
    }

    fun isPieceAt(position: Position): Boolean {
        return isPieceAt(position.x, position.y)
    }

    fun isBlankAt(x: Int, y: Int): Boolean {
        return !isPieceAt(x, y)
    }

    fun isBlankAt(position: Position): Boolean {
        return !isPieceAt(position)
    }

    fun isAllyAt(x: Int, y: Int, piece: ChessPiece): Boolean {
        return pieceAt(x, y)?.isAlly(piece) ?: false
    }

    fun isAllyAt(position: Position, piece: ChessPiece): Boolean {
        return isAllyAt(position.x, position.y, piece)
    }

    fun isOpponentAt(x: Int, y: Int, piece: ChessPiece): Boolean {
        val other = pieceAt(x, y) ?: return false
        return other.isOpponent(piece)
    }

    fun isOpponentAt(position: Position, piece: ChessPiece): Boolean {
        return isOpponentAt(position.x, position.y, piece)
    }

    fun inBounds(coordinate: Int): Boolean

    fun inBounds(x: Int, y: Int): Boolean {
        return inBounds(x) && inBounds(y)
    }

}