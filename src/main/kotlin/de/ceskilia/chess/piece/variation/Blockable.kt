package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.util.addPinMoves
import de.ceskilia.chess.util.calculateExtendedMovesPinned

interface Blockable : ChessPiece {

    override fun calculateMoves(): Set<Position> {
        return calculateExtendedMovesPinned()
    }

    override fun calculateCoveringMoves(): Set<Position> {
        return calculateLines()
            .flatten()
            .toSet()
    }

    fun isPinning(other: ChessPiece): Boolean {
        return other !is AbstractKing && calculatePinLine(other) != null
    }

    fun calculatePinLine(other: ChessPiece): Set<Position>? {
        return calculatePinLines().firstOrNull { line ->
            val hasKing = line.any { board.pieceAt(it) is AbstractKing }
            val hasPiece = line.any { board.pieceAt(it) == other }
            hasKing && hasPiece
        }
    }

    fun calculatePinLines(): List<Set<Position>> {
        return calculateLines().map { addPinMoves(it) }
    }

    fun calculateLines(): List<Set<Position>>

}