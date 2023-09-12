package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractKing

/**
 * This interface is used to represent [ChessPiece]s that can be checked, restricting the legal moves of one color.
 *
 * Checkable pieces cannot pin other pieces, be pinned or captured. They also are unable to move to positions that
 * are [covered][isCoveredByOpponent] by an opponent piece.
 *
 * One common example of this is [AbstractKing].
 *
 * NOTE: Checkable pieces of one type cannot check other checkable pieces with that same type!
 */
interface Checkable : ChessPiece {

    /**
     * Returns the moves this movable can make independent of it being pinned or not.
     *
     * Checkable pieces are unable to move to [positions][Position] that are [covered][isCoveredByOpponent]
     * by an opponent piece.
     *
     * @return the moves the movable can make independent of being pinned
     * @see calculateMoves
     */
    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()

        moves.removeIf {
            val isAlly = board.isAllyAt(it, this)
            val isCovered = it.isCoveredByOpponent()

            isAlly || isCovered
        }

        return moves
    }

    /**
     * Returns true if an opponent piece could move to the current position of this piece, therefore checking it.
     *
     * NOTE: Checkable pieces can not check other pieces.
     *
     * @return true if the piece is checked by another piece
     */
    fun isChecked(): Boolean {
        return board.opponentPieces(color)
            .filter { it::class != this::class }
            .any { it.canMoveTo(this.position) }
    }

}