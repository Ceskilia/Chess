package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

abstract class ChessPiece(
    val board: Chessboard,
    var position: Position,
    val color: Color,
    val type: Type
) {

    fun isAlly(other: ChessPiece): Boolean {
        return this.color == other.color
    }

    fun isOpponent(other: ChessPiece): Boolean {
        return !isAlly(other)
    }

    // todo: getOpponentPieces/getAllyPieces here or in chessPiece

    fun moveTo(position: Position) {
        board.move(this, position)
    }

    fun canMoveTo(position: Position): Boolean {
        return calculateMoves().contains(position)
    }

    fun hasMoves(): Boolean {
        return calculateMoves().isNotEmpty()
    }

    fun calculateBlockingMoves(
        checkingPiece: BlockableChessPiece,
        ownKing: ChessPiece = board.getPieces(color).first { it.type == Type.KING }
    ): Set<Position> {
        val moves = mutableSetOf<Position>()
        val possibleMoves = calculateMoves()

        for (line in checkingPiece.calculateLines()) {
            val isCheckingLine = line.contains(ownKing.position)

            // check if this is the checking line
            if (!isCheckingLine) {
                continue
            }

            for (position in line) {
                if (possibleMoves.contains(position)) {
                    moves += position
                }
            }

        }

        return moves
    }

    open fun calculateMoves(): Set<Position> {
        return calculateCoveringMoves()
    }

    // the term "covering" refers to the fact, that some pieces (pawns) can
    // make moves, that cannot capture a piece, so we need to differentiate between those to make it possible
    // to check for possible moves of pieces with type KING
    fun isCovering(position: Position): Boolean {
        return calculateCoveringMoves().contains(position)
    }

    abstract fun calculateCoveringMoves(): Set<Position>

    enum class Color {

        BLACK,
        WHITE

    }

    enum class Type(val notation: Char, val value: Int) {

        PAWN('P', 1),
        KNIGHT('N', 3),
        BISHOP('B', 3),
        ROOK('R', 5),
        QUEEN('Q', 9),
        KING('K', -1);

        // get image from color

    }

}