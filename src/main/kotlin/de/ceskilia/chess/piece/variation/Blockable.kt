package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.util.addPinMoves
import de.ceskilia.chess.util.calculateExtendedMovesPinned

/**
 * This interface is used to represent [ChessPiece]s that calculate their moves based on an arithmetic pattern,
 * grouping *lines*. Therefore, blockables are able to [pin][calculatePinLine] other pieces.
 *
 * Blockable pieces can be interrupted when checking a [Checkable]: opponent pieces can move in the line, *blocking*
 * the check.
 *
 * The term **line** refers to a subset of moves of a [Blockable], representing that their moves can be processed and
 * separately grouped based on an arithmetic pattern. These lines together constitute the move set of this blockable.
 */
interface Blockable : ChessPiece {

    override fun calculateMoves(): Set<Position> {
        return calculateExtendedMovesPinned()
    }

    override fun calculateCoveringMoves(): Set<Position> {
        return calculateLines()
            .flatten()
            .toSet()
    }

    /**
     * Returns true if this piece is pinning the [provided piece][other].
     *
     * NOTE: [Checkable pieces][Checkable] cannot be pinned.
     *
     * @param other the piece to check
     *
     * @return true if the piece is pinning the provided one
     * @see calculatePinLine
     */
    fun isPinning(other: ChessPiece): Boolean {
        return other !is Checkable && calculatePinLine(other) != null
    }

    /**
     * Returns a collection of moves that represents the **line** pinning the [provided piece][other].
     *
     * May be null if no such line exists.
     *
     * NOTE: This includes the first occurring opponent piece, [checkable pieces][Checkable], but no allies!
     *
     * The term **line** refers to a subset of moves of a [Blockable], representing that their moves can be processed
     * and separately grouped based on an arithmetic pattern. These lines together constitute the move set of this
     * blockable.
     *
     * @param other the piece to calculate the line for
     *
     * @return the moves of this blockable that contribute in the pinning of the provided piece
     */
    fun calculatePinLine(other: ChessPiece): Set<Position>? {
        return calculatePinLines().firstOrNull { line ->
            val hasCheckable = line.any { board.pieceAt(it) is Checkable }
            val hasProvidedPiece = line.any { board.pieceAt(it) == other }

            hasCheckable && hasProvidedPiece
        }
    }

    /**
     * Returns an extended collection of the [*lines*][calculateLines] of this blockable.
     *
     * This continues the arithmetic processing of the lines which stopped at an opponent piece, extending the line to
     * the point of encountering a piece. This is inclusive for opponent pieces of type [Checkable], allowing the
     * check of specific pin lines.
     *
     * The term **line** refers to a subset of moves of a [Blockable], representing that their moves can be processed
     * and separately grouped based on an arithmetic pattern. These lines together constitute the move set of this
     * blockable.
     *
     * @return an extended collection of the lines of the blockable
     * @see calculatePinLine
     */
    fun calculatePinLines(): List<Set<Position>> {
        return calculateLines().map { addPinMoves(it) }
    }

    /**
     * Returns a collection of grouped moves, moreover *lines*, representing the individual subsets
     * of an arithmetic pattern based procession.
     *
     * This represents [*covering*][calculateCoveringMoves] moves and ignores any occurrences of pieces with type
     * [Checkable]. Therefore, the processing stops when encountering an ally or an opponent piece with a different
     * type than [Checkable]. Both scenarios are inclusive!
     *
     * The term **line** refers to a subset of moves of a [Blockable], representing that their moves can be processed
     * and separately grouped based on an arithmetic pattern. These lines together constitute the move set of this
     * blockable.
     *
     * @return a collection of lines
     */
    fun calculateLines(): List<Set<Position>>

}