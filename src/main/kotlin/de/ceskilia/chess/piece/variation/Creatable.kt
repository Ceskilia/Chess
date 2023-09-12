package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.piece.ChessPiece

/**
 * This interface is used to represent [ChessPiece]s that can be created for a promotion.
 *
 * @see Promotable
 */
interface Creatable : ChessPiece {

    /**
     * Returns a copy of this object.
     *
     * NOTE: The type should be identical to the one of this object!
     *
     * @return a copy of this creatable
     */
    fun createCopy(): Creatable

}