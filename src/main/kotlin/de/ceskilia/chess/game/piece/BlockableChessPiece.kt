@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.addPinMoves

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
        return other.type != Type.KING && calculatePinLine(other) != null
    }

    override fun calculateMoves(): Set<Position> {
        val moves = calculateMovesUnpinned()
        val pinningPiece = getPinningPiece() ?: return moves
        return pinningPiece.calculatePinLines()
            .firstOrNull { line -> line.any { board.getPieceAt(it)?.type == Type.KING } }!!
            .plus(pinningPiece.position)
            .filter { moves.contains(it) }
            .toSet()
    }

    fun calculatePinLine(other: ChessPiece): Set<Position>? {
        return calculatePinLines()
            .firstOrNull { line ->
                val hasKing = line.any { board.getPieceAt(it)?.type == Type.KING }
                val hasPiece = line.any { board.getPieceAt(it) == other }
                hasKing && hasPiece
            }
    }

    open fun calculatePinLines(): List<Set<Position>> {
        return calculateLines().map { addPinMoves(it) }
    }

    abstract fun calculateLines(): List<Set<Position>>

}