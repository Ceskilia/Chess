@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Checkable
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.piece.variation.Promotable
import de.ceskilia.chess.util.notNegative

abstract class Chessboard(
    val game: ChessGame,
    val size: Int = DEFAULT_SIZE,
    protected val table: InternalTable = InternalTable(size)
) : PieceDataHandler by table {

    companion object {

        const val DEFAULT_SIZE = 8

    }

    init {
        notNegative(size) { "Size may not be negative. Provided: $size" }
    }

    abstract val registeredCreatables: List<Creatable>

    private val queuedMoveActions = mutableMapOf<ChessPiece, MutableSet<Action>>()

    open fun move(piece: ChessPiece, endPosition: Position): Move? {

        if (!piece.canMoveTo(endPosition)) {
            return null
        }

        val startPosition = piece.position
        val (cancel, capturedPiece) = evaluateQueuedAction(piece, endPosition)
        val endPiece = capturedPiece ?: pieceAt(endPosition)

        if (!cancel) {
            moveUnchecked(piece, endPosition) // this or promote so PAWNS don't get to last rank
        }

        val check = opponentPieces(piece.color)
            .filterIsInstance<Checkable>()
            .any(Checkable::isChecked)

        pieces(piece.color)
            .filterIsInstance<Promotable>()
            .filter(Promotable::canPromote)
            .forEach {
                val promotable = it.promote()
                placePiece(promotable, promotable.position)
            }

        return Move(piece, endPiece, startPosition, endPosition, check)
    }

    open fun move(startPosition: Position, endPosition: Position): Move? {
        val piece = pieceAt(startPosition)
        return if (piece != null) move(piece, endPosition) else null
    }

    fun syncPieces() {
        // sync: board is right
        for (y in table.pieces.indices) {
            for (x in table.pieces[y].indices) {
                val piece = pieceAt(x, y) ?: continue
                val position = piece.position

                // check if it is synced already
                if (position.x == x && position.y == y) {
                    continue
                }

                piece.position = Position.of(x, y)
            }
        }
    }

    fun encodeCurrentState(): Long {
        return pieces().sumOf(ChessPiece::encodeState)
    }

    fun queueMoveAction(
        piece: ChessPiece,
        position: Position,
        result: Action.Result,
        action: (Position) -> Unit
    ) {
        queuedMoveActions.compute(piece) { _, value ->
            val actions = value ?: mutableSetOf()
            if (actions.none { it.position == position })
                actions.add(Action(position, result, action))
            return@compute actions
        }
    }

    private fun evaluateQueuedAction(piece: ChessPiece, endPosition: Position): Action.Result {
        val actionMovingTo = queuedMoveActions[piece]
            ?.firstOrNull { it.position == endPosition }
        queuedMoveActions.remove(piece)

        if (actionMovingTo == null) {
            return Action.Result.DEFAULT
        }

        actionMovingTo.action.invoke(endPosition)
        return actionMovingTo.result
    }

    override fun toString(): String {
        return buildString {

            for (row in 0 until size) {
                append("  ")
                append("+---".repeat(size) + '+')
                append("\n")
                append("${row + 1} ")

                for (column in 0 until size) {
                    append("| ${pieceAt(column, row)?.notation ?: " "} ")
                }

                append("|")
                append("\n")
            }

            append("  ")
            append("+---".repeat(size) + '+')
            append("\n")
            append("  ")

            for (column in 0 until size) {
                append(" ${Position.coordinateToLetter(column)}  ")
            }

        }
    }

    abstract fun directionOf(pawn: AbstractPawn): Int

    abstract fun setup()

}