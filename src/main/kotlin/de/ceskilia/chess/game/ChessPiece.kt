package de.ceskilia.chess.game

import de.ceskilia.chess.game.board.Chessboard

abstract class ChessPiece(
    val board: Chessboard,
    var position: Position,
    val color: Color,
    val type: Type
) {

    fun move(position: Position) {
        board.move(this, position)
    }

    fun canMoveTo(position: Position): Boolean {
        return calculateMoves().contains(position)
    }

    abstract fun calculateMoves(): List<Position>

    enum class Color {

        BLACK,
        WHITE

    }

    enum class Type(val notation: Char) {

        PAWN('P'),
        KNIGHT('N'),
        BISHOP('B'),
        ROOK('R'),
        QUEEN('Q'),
        KING('K');

        // get image from color

    }

}