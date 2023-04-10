package de.ceskilia.chess.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Action
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.standardtype.AbstractKing

interface ChessPiece : Movable {

    val board: Chessboard
    var position: Position // todo: rearrange
    val color: Color
    val notation: Char

    fun Position.isCoveredByOpponent(): Boolean {
        return board.opponentPieces(color).any { it.isCovering(this) }
    }

    fun Position.whenOccupied(result: Action.Result = Action.Result.DEFAULT, action: (Position) -> Unit): Position {
        board.queueMoveAction(this@ChessPiece, this, result, action)
        return this
    }

    // find other solution: this hash codes are not made for uniqueness
    fun encodeState(): Long {
        var result = board.hashCode().toLong()

        result *= 31 * position.x // coordinates need to have different impact [ P(2, 3) would be the same as P(3, 2) ]
        result *= 43 * position.y
        result = 31 * result + color.hashCode()
        result = 31 * result + notation.hashCode()

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

    fun calculateBlockingMoves(checkingPiece: Blockable, king: AbstractKing? = null): Set<Position> {
        val moves = mutableSetOf<Position>()
        val possibleMoves = calculateMoves()
        val ownKing = king ?: board.pieces(color)
            .filterIsInstance<AbstractKing>()
            .first()

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

}