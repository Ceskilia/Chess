package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.data.PieceDataHandler
import de.ceskilia.chess.game.board.internal.InternalTable
import de.ceskilia.chess.game.board.internal.MoveActionHandler
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Checkable
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.piece.variation.Promotable
import de.ceskilia.chess.util.notNegative

abstract class Chessboard(
    val game: ChessGame,
    val size: Int = DEFAULT_SIZE,
) : PieceDataHandler by InternalTable(size) {

    companion object {

        const val DEFAULT_SIZE = 8

    }

    init {
        notNegative(size) { "Size may not be negative. Provided: $size" }
    }

    abstract val registeredCreatables: List<Creatable>

    internal val moveActionHandler = MoveActionHandler()

    open fun move(piece: ChessPiece, endPosition: Position): Move? {

        if (!piece.canMoveTo(endPosition)) {
            return null
        }

        val startPosition = piece.position
        val (cancel, capturedPiece) = moveActionHandler.evaluateQueuedAction(piece, endPosition)
        val endPiece = capturedPiece ?: pieceAt(endPosition)

        if (!cancel) {
            moveUnchecked(piece, endPosition) // this or promote so PAWNS don't get to last rank
        }

        val isCheck = opponentPieces(piece.color)
            .filterIsInstance<Checkable>()
            .any(Checkable::isChecked)

        pieces(piece.color)
            .filterIsInstance<Promotable>()
            .filter(Promotable::canPromote)
            .forEach {
                val promotionContext = it.promote()

                removePiece(it)
                placePiece(promotionContext.creatable, promotionContext.position)
            }

        return Move(
            chessPiece = piece,
            capturedPiece = endPiece,
            startPosition = startPosition,
            endPosition = endPosition,
            isCheck = isCheck
        )
    }

    open fun move(startPosition: Position, endPosition: Position): Move? {
        val piece = pieceAt(startPosition)
        return if (piece != null) move(piece, endPosition) else null
    }

    fun promotePiece(color: ChessPiece.Color, typeName: String): Creatable? {
        val creatable = registeredCreatables
            .filter { it::class.java.simpleName == typeName }
            .firstOrNull { it.color == color }
            ?: return null
        return validateCopy(creatable)
    }

    fun promotePiece(color: ChessPiece.Color, type: Class<Creatable>): Creatable {
        val creatable = registeredCreatables
            .filter { it::class.java == type }
            .firstOrNull { it.color == color }
            ?: throw IllegalArgumentException(
                "The provided creatable with color=$color and type=${type::class.java.name} is not registered."
            )
        return validateCopy(creatable)
    }

    private fun validateCopy(creatable: Creatable): Creatable {
        val copy = creatable.createCopy()
        check(copy::class == creatable::class) {
            "The provided creatable does not create a copy of its own type."
        }
        return copy
    }

    abstract fun directionOf(pawn: AbstractPawn): Int

    abstract fun setup()

    enum class Side(val direction: Int) {

        KING_SIDE(1),
        QUEEN_SIDE(-1)

    }

}