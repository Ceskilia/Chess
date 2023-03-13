package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

interface ChessPiece : Movable {

    val board: Chessboard
    var position: Position
    val color: Color
    val type: Type

    fun Position.isCoveredByOpponent(): Boolean {
        return board.opponentPieces(color).any { it.isCovering(this) }
    }

    fun Position.whenOccupied(cancel: Boolean = false, action: (Position) -> Unit): Position {
        board.queueMoveAction(this@ChessPiece, this, cancel, action)
        return this
    }

    // find other solution: this hash codes are not made for uniqueness
    fun encodeState(): Long {
        var result = board.hashCode().toLong()

        result *= 31 * position.x // coordinates need to have different impact
        result *= 43 * position.y // elso P(2, 3) would be the same as P(3, 2)
        result = 31 * result + color.hashCode()
        result = 31 * result + type.hashCode()

        return result
    }

    fun isAlly(other: ChessPiece): Boolean {
        return this.color == other.color
    }

    fun isOpponent(other: ChessPiece): Boolean {
        return !isAlly(other)
    }

    fun isPinned(): Boolean {
        return pinningPiece() != null
    }

    fun pinningPiece(): Blockable? {
        return board.opponentPieces(color)
            .filterIsInstance<Blockable>()
            .firstOrNull { it.isPinning(this) }
    }

    fun calculateBlockingMoves(checkingPiece: Blockable, king: ChessPiece? = null): Set<Position> {
        val moves = mutableSetOf<Position>()
        val possibleMoves = calculateMoves()
        val ownKing = king ?: board.pieces(color).first { it.type == Type.KING }

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

    override fun moveTo(position: Position) {
        board.move(this, position)
    }

    override fun calculateMoves(): Set<Position> {
        return if (isPinned()) emptySet() else calculateMovesUnpinned()
    }

    override fun calculateMovesUnpinned(): Set<Position> {
        return calculateCoveringMoves()
            .filter { !board.isAllyAt(it, this) }
            .toSet()
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