@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.calculatePinMoves

abstract class BlockableChessPiece(
    board: Chessboard,
    position: Position,
    color: Color,
    type: Type
) : ChessPiece(board, position, color, type) {

    override fun calculateCoveringMoves(): Set<Position> {
        return calculateLines()
            .flatten()
            .toSet()
    }

    fun isPinning(other: ChessPiece): Boolean {
        return calculatePinLines()
            .flatten()
            .contains(other.position)
    }

    open fun calculatePinLines(): List<Set<Position>> {
        return calculateLines().map { calculatePinMoves(it) }
    }

    abstract fun calculateLines(): List<Set<Position>>

}