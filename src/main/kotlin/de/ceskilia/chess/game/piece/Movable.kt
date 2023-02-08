package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Movable {


    fun moveTo(position: Position)

    fun canMoveTo(position: Position): Boolean {
        return calculateMoves().contains(position)
    }

    fun hasMoved(): Boolean

    fun hasMoves(): Boolean {
        return calculateMoves().isNotEmpty()
    }

    fun calculateMoves(): Set<Position>

    fun calculateMovesUnpinned(): Set<Position> {
        return calculateCoveringMoves()
    }

    // the term "covering" refers to the fact, that some pieces (pawns) can
    // make moves, that cannot capture a piece, so we need to differentiate between those to make it possible
    // to check for possible moves of pieces with type KING
    fun isCovering(position: Position): Boolean {
        return calculateCoveringMoves().contains(position)
    }

    fun calculateCoveringMoves(): Set<Position>

}