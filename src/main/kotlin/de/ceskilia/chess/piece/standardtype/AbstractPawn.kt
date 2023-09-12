package de.ceskilia.chess.piece.standardtype

import de.ceskilia.chess.piece.variation.Promotable

/**
 * This interface is used to represent [Promotable]s that can be promoted when they reach the last rank of an opponent.
 *
 * It also defines an abstract common type for chess pieces that have a default behaviour of pawns.
 */
interface AbstractPawn : Promotable {

    /**
     * The y-direction of this pawn based on its color.
     */
    val direction: Int
        get() = board.directionOf(this)

    /**
     * Returns true if this pawn is able to get promoted.
     *
     * Pawns can get promoted when they are at the last rank of an opponent.
     *
     * @return true if the pawn can be promoted
     * @see promote
     */
    override fun canPromote(): Boolean {
        return !board.inBounds(position.y + direction)
    }

}