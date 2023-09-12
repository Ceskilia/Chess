package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractPawn

/**
 * This interface is used to represent [ChessPiece]s that can be converted to a higher piece, moreover *promoted*,
 * when fulfilling a specific condition.
 *
 * One common example of this is [AbstractPawn].
 *
 * @see Creatable
 */
interface Promotable : ChessPiece {

    /**
     * Returns true if this promotable is able to get promoted.
     *
     * @return true if the promotable can be promoted
     * @see promote
     */
    fun canPromote(): Boolean

    /**
     * Returns a [Context] for the promotion of this promotable.
     *
     * The context must contain the [created piece][Creatable] and the initial [position][Position] for it.
     *
     * @return the context for the promotion
     * @see Chessboard.promotePiece
     */
    fun promote(): Context

    /**
     * Represents the context of a promotion.
     *
     * @property creatable the created piece
     * @property position the position for the created piece
     * @constructor create a promotion context
     */
    data class Context(val creatable: Creatable, val position: Position)

}