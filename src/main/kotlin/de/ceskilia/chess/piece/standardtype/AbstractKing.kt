package de.ceskilia.chess.piece.standardtype

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.annotation.Interchangeable
import de.ceskilia.chess.piece.variation.Checkable
import de.ceskilia.chess.util.checkCastleDirection

/**
 * This interface is used to represent [Checkable]s that can be checked, restricting the legal moves of one color.
 *
 * It also defines an abstract common type for chess pieces that have a default behaviour of kings.
 *
 * Abstract kings cannot pin other pieces, be pinned or captured. They also are unable to move to positions that are
 * [covered][isCoveredByOpponent] by an opponent piece.
 *
 * Additionally, abstract kings can [castle](https://en.wikipedia.org/wiki/Castling) on all [sides][Chessboard.Side]
 * of a chessboard with an [Interchangeable] under certain conditions.
 *
 * NOTE: Checkable pieces of one type cannot check other checkable pieces with that same type!
 */
interface AbstractKing : Checkable {

    override fun calculateMovesUnpinned(): Set<Position> {
        val moves = super.calculateMovesUnpinned()
        val castleMoves = Chessboard.Side.values()
            .map(this::checkCastle)
            .flatten()
        return moves.plus(castleMoves)
    }

    /**
     * Returns the moves this king can make in order to [castle](https://en.wikipedia.org/wiki/Castling) to the
     * provided [side].
     *
     * This will return an empty set if this king does not fulfill the following conditions:
     *  * He did not castle already.
     *  * He is not in check.
     *  * He did not move.
     *  * The selected interchangeable did not move.
     *  * The fields between the king and the interchangeable are empty.
     *  * The fields the king would theoretically move to, to get to his position, are not [covered][isCoveredByOpponent].
     *
     * @param side the side to check for castling
     *
     * @return a set of moves representing the castle moves
     */
    fun checkCastle(side: Chessboard.Side): Set<Position> {
        if (hasMoved() || isChecked())
            return emptySet()
        return checkCastleDirection(side)
    }

}