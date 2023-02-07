@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

abstract class ChessPiece(
    val board: Chessboard,
    var position: Position,
    val color: Color,
    val type: Type
) : Movable {

    fun isAlly(other: ChessPiece): Boolean {
        return this.color == other.color
    }

    fun isOpponent(other: ChessPiece): Boolean {
        return !isAlly(other)
    }

    fun isPinned(): Boolean {
        return getPinningPiece() != null
    }

    fun getPinningPiece(): BlockableChessPiece? {
        return board.getOpponentPieces(color)
            .filterIsInstance<BlockableChessPiece>()
            .firstOrNull { it.isPinning(this) }
    }

    fun calculateBlockingMoves(checkingPiece: BlockableChessPiece, king: ChessPiece? = null): Set<Position> {
        val moves = mutableSetOf<Position>()
        val possibleMoves = calculateMoves()
        val ownKing = king ?: board.getPieces(color).first { it.type == Type.KING }

        check(isAlly(ownKing)) { "The provided king is not an ally. Expected: $color, Provided: ${ownKing.color}." }

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

    override fun calculateMoves(): Set<Position> {
        return if (isPinned()) emptySet() else calculateMovesUnpinned()
    }

    override fun moveTo(position: Position) {
        board.move(this, position)
    }

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