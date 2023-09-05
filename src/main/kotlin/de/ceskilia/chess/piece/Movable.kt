package de.ceskilia.chess.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Movable {

    fun onMove() {}

    fun moveTo(position: Position)

    fun canMoveTo(position: Position): Boolean {
        return calculateMoves().contains(position)
    }

    fun hasMoved(): Boolean

    fun hasMoves(): Boolean {
        return calculateMoves().isNotEmpty()
    }

    fun calculateMoves(): Set<Position>

    fun calculateMovesUnpinned(): Set<Position>

    // the term "covering" refers to the fact, that some movables (pawns) can make moves, that cannot capture another
    // movable, so there needs to be a differentiation between those to make it possible to check for possible moves
    // of some movables (kings)
    // SO
    // it includes all the theoretical possible "covering" moves the movable can make
    fun isCovering(position: Position): Boolean {
        return calculateCoveringMoves().contains(position)
    }

    fun calculateCoveringMoves(): Set<Position>

}