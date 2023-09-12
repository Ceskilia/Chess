package de.ceskilia.chess.piece

import de.ceskilia.chess.game.arithmetic.Position

/**
 * This interface is used to define and abstract the behaviour of specific objects, moreover [*pieces*][ChessPiece].
 */
interface Movable {

    /**
     *  This method is called when this movable changes its position.
     */
    fun onMove() {}

    /**
     * Moves the movable to another [position].
     *
     * This movable must be able to move to the position.
     *
     * @param position the position to move to
     * @see canMoveTo
     */
    fun moveTo(position: Position)

    /**
     * Returns true if the provided [position] is a possible move this movable can make.
     *
     * @return true if the movable can move to the provided position
     * @see calculateMoves
     */
    fun canMoveTo(position: Position): Boolean {
        return calculateMoves().contains(position)
    }

    /**
     * Returns true if this movable didn't do a move or didn't was moved yet.
     *
     * @return true if the movable didn't move yet
     * @see moveTo
     * @see de.ceskilia.chess.game.board.Chessboard.move
     */
    fun hasMoved(): Boolean

    /**
     * Return true if this movable has any available moves.
     *
     * @return true if the movable has any moves
     * @see calculateMoves
     */
    fun hasMoves(): Boolean {
        return calculateMoves().isNotEmpty()
    }

    /**
     * Returns the legal moves this movable can make.
     *
     * This includes being pinned by another movable, therefore restricting the [unpinned moves][calculateMovesUnpinned].
     *
     * Moreover, this also refers to moves that are not able to capture any pieces and therefore need to be processed
     * separately.
     *
     * See [calculateCoveringMoves] for an unfiltered move set.
     *
     * @return the legal moves the movable can make
     * @see calculateCoveringMoves
     */
    fun calculateMoves(): Set<Position>

    /**
     * Returns the moves this movable can make independent of it being pinned or not.
     *
     * Moreover, this also refers to moves that are not able to capture any pieces and therefore need to be processed
     * separately.
     *
     * @return the moves the movable can make independent of being pinned
     * @see calculateMoves
     */
    fun calculateMovesUnpinned(): Set<Position>

    /**
     * Returns true if the provided [position] is a possible covering move this movable can make.
     *
     * The term **covering** refers to an unfiltered representation of the move set of a [Movable].
     * It does not include any restrictions like capturing own pieces or being pinned.
     *
     * @return true if the movable is covering the provided position
     * @see calculateCoveringMoves
     */
    fun isCovering(position: Position): Boolean {
        return calculateCoveringMoves().contains(position)
    }

    /**
     * Returns the moves based on the move pattern of this movable.
     *
     * The term **covering** refers to an unfiltered representation of the move set of a [Movable].
     * It does not include any restrictions like capturing own pieces or being pinned.
     *
     * See [calculateMoves] for a filtered move set
     *
     * @return the unfiltered moves the movable can make
     * @see calculateMoves
     */
    fun calculateCoveringMoves(): Set<Position>

}