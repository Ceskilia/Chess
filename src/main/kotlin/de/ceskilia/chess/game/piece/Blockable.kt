package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Blockable : Movable {

    override fun calculateCoveringMoves(): Set<Position> {
        return calculateLines().flatten().toSet()
    }

    fun calculateLines(): List<Set<Position>>

}