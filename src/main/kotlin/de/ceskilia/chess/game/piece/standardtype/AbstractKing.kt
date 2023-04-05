package de.ceskilia.chess.game.piece.standardtype

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.Checkable
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.checkCastleDirection

interface AbstractKing : Checkable {

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()

        moves.removeIf {
            val isAlly = board.isAllyAt(it, this)
            val isCovered = it.isCoveredByOpponent()

            isAlly || isCovered
        }

        return moves.plus(checkCastle())
    }

    fun checkCastle(): Set<Position> {

        if (hasMoved() || isChecked()) {
            return emptySet()
        }

        return checkCastleDirection(Operation.INCREMENT) + checkCastleDirection(Operation.DECREMENT)
    }

}