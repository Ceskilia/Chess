 @file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.Checkable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.Promotable
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
        val endPiece = pieceAt(endPosition)
        val cancelled = evaluateQueuedAction(piece, endPosition)

        if (!cancelled) {
            moveUnchecked(piece, endPosition) // this or promote so PAWNS don't get to last rank
        }

        val check = opponentPieces(piece.color)
            .filterIsInstance<Checkable>()
            .any(Checkable::isChecked)

        pieces(piece.color)
            .filterIsInstance<Promotable>()
            .filter(Promotable::canPromote)
            .forEach { placePiece(it.promote(), it.position) }

        return Move(piece, startPosition, endPosition, check, endPiece)
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
        cancel: Boolean,
        action: (Position) -> Unit
    ) {
        queuedMoveActions.compute(piece) { _, value ->
            val actions = value ?: mutableSetOf()
            if (actions.none { it.position == position })
                actions.add(Action(position, cancel, action))
            return@compute actions
        }
    }

    private fun evaluateQueuedAction(piece: ChessPiece, endPosition: Position): Boolean {
        val actionMovingTo = queuedMoveActions[piece]
            ?.firstOrNull { it.position == endPosition }
        queuedMoveActions.remove(piece)

        if(actionMovingTo == null) {
            return false
        }

        actionMovingTo.action.invoke(endPosition)
        return actionMovingTo.cancel
    }

    override fun toString(): String {
        val builder = StringBuilder()

        for (row in 0 until size) {
            builder.append("  ")
                .append("+---".repeat(size) + '+')
                .append("\n")
                .append("${row + 1} ")

            for (column in 0 until size) {
                builder.append("| ${pieceAt(column, row)?.type?.notation ?: " "} ")
            }

            builder.append("|")
                .append("\n")
        }

        builder.append("  ")
            .append("+---".repeat(size) + '+')
            .append("\n")
            .append("  ")

        for (column in 0 until size) {
            builder.append(" ${Position.coordinateToLetter(column)}  ")
        }

        return builder.toString()
    }

    abstract fun directionOf(color: ChessPiece.Color): Int

    abstract fun setup()

}