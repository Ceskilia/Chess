package de.ceskilia.chess.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.internal.Action
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.variation.Blockable
import de.ceskilia.chess.piece.variation.Checkable

/**
 * This interface is used to represent the behaviour of figures (or *pieces*) on a [Chessboard].
 *
 * Normal chess pieces cannot be interrupted, moreover [blocked][Blockable], when checking a [Checkable].
 */
interface ChessPiece : Movable {

    /**
     * The chessboard this piece is placed on.
     */
    val board: Chessboard

    /**
     * The color of this piece, grouping it with others.
     */
    val color: Color

    /**
     * The notation of this piece, used for [move representation][de.ceskilia.chess.game.arithmetic.Move.toString].
     */
    val notation: Char

    /**
     * The position of this piece, calculated based on the [board] it is placed on.
     */
    val position: Position
        get() = board.positionOf(this)

    override fun moveTo(position: Position) {
        board.move(this, position)
    }

    /**
     * Returns the legal moves this movable can make.
     *
     * This includes being pinned by another movable, therefore restricting the [unpinned moves][calculateMovesUnpinned].
     *
     * See [calculateCoveringMoves] for an unfiltered move set.
     *
     * NOTE: This type of chess pieces do not have any moves when pinned.
     *
     * @return the legal moves the movable can make
     * @see Movable.calculateMoves
     * @see Blockable
     */
    override fun calculateMoves(): Set<Position> {
        return if (isPinned()) emptySet() else calculateMovesUnpinned()
    }

    override fun calculateMovesUnpinned(): Set<Position> {
        return calculateCoveringMoves()
            .filter { !board.isAllyAt(it, this) }
            .toSet()
    }

    /**
     * Returns true if an opponent piece is [covering][ChessPiece.isCovering] the provided position.
     *
     * @receiver [Position]
     *
     * @return true if the position is covered
     */
    fun Position.isCoveredByOpponent(): Boolean {
        return board.opponentPieces(color)
            .any { it.isCovering(this) }
    }

    /**
     * Returns the receiver and is queueing an [action] with the provided position.
     *
     * The action is executed when this piece has moved to the given position.
     *
     * The [result] is used to indicate how to handle the default implementation on processing moves.
     *
     * @receiver [Position]
     *
     * @param [result] the result on how to handle the default processing
     * @param [action] the action that should be executed when moving to the position
     *
     * @return the receiver position, used for chaining
     * @see [Action.Result]
     */
    fun Position.whenOccupying(
        result: Action.Result = Action.Result.DEFAULT,
        action: (Position) -> Unit
    ): Position {
        board.moveActionHandler.queueMoveAction(
            piece = this@ChessPiece,
            position = this,
            result = result,
            action = action
        )
        return this
    }

    /**
     * Returns true if the provided piece has the same color as this piece.
     *
     * @param other the piece to check
     *
     * @return true if the provided piece has the same color
     */
    fun isAlly(other: ChessPiece): Boolean {
        return this.color == other.color
    }

    /**
     * Returns true if the provided piece has a different color compared to this piece.
     *
     * @param other the piece to check
     *
     * @return true if the provided piece has a different color
     */
    fun isOpponent(other: ChessPiece): Boolean {
        return !isAlly(other)
    }

    /**
     * Returns true if this chess piece is pinned by another piece.
     *
     * @return true if this piece is pinned
     *
     * @see pinningPiece
     */
    fun isPinned(): Boolean {
        return pinningPiece() != null
    }

    /**
     * Returns the [piece][Blockable] that is pinning this piece, therefore restricting its moves.
     *
     * If there is none, this will return null.
     *
     * NOTE: Only blockable pieces are able to pin other chess pieces.
     *
     * @return the piece that is pinning this chess piece
     * @see Blockable
     */
    fun pinningPiece(): Blockable? {
        return board.opponentPieces(color)
            .filterIsInstance<Blockable>()
            .firstOrNull { it.isPinning(this) }
    }

    /**
     * Returns the moves of this chess piece that are able to block the check of the provided [checkingPiece].
     *
     * If no [checkable] is provided, the king with the color of this chess piece is used.
     *
     * @return the check blocking moves of this piece
     *
     * @throws IllegalArgumentException if the provided checkable is not of the same color
     */
    fun calculateBlockingMoves(checkingPiece: Blockable, checkable: Checkable? = null): Set<Position> {
        val moves = mutableSetOf<Position>()
        val possibleMoves = calculateMoves()
        val ownCheckable = checkable ?: this.board.pieces(this.color)
            .filterIsInstance<Checkable>()
            .first()

        check(isAlly(ownCheckable)) { "The provided king is not an ally. Expected: $color, Provided: ${ownCheckable.color}." }

        for (line in checkingPiece.calculateLines()) {
            val isCheckingLine = line.contains(ownCheckable.position)

            // check if this is the checking line
            if (!isCheckingLine) {
                continue
            }

            for (position in line) {
                if (possibleMoves.contains(position)) {
                    moves += position
                }
            }

        }

        return moves
    }

    /**
     * The color of a chess piece.
     */
    enum class Color {

        BLACK,
        WHITE

    }

}