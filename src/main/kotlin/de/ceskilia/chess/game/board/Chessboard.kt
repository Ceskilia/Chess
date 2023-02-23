@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.Promotable
import de.ceskilia.chess.util.notNegative

abstract class Chessboard(
    val game: ChessGame,
    val size: Int = DEFAULT_SIZE,
) {

    companion object {

        const val DEFAULT_SIZE = 8

    }

    init {
        notNegative(size) { "Size may not be negative. Provided: $size" }
    }

    abstract val registeredCreatables: List<Creatable>

    protected val queuedMoveActions = mutableMapOf<ChessPiece, MutableSet<Pair<Position, () -> Unit>>>()
    protected val board: List<MutableList<ChessPiece?>> = List(size) {
        MutableList(size) { null }
    }

    // TODO: CHECKS
    open fun move(piece: ChessPiece, endPosition: Position): Move? {

        if (!piece.canMoveTo(endPosition)) {
            return null
        }

        val startPosition = piece.position
        val endPiece = board[endPosition.y][endPosition.x]

        executeQueuedAction(piece, endPosition)
        piece.position = endPosition
        board[endPosition.y][endPosition.x] = piece
        removePieceAt(startPosition)

        // todo: maybe use Player
        val check = getPieces(piece.color).any { ally ->
            ally.canMoveTo(getOpponentPieces(piece.color)
                .first { it.type == ChessPiece.Type.KING }
                .position
            )
        }

        if(piece is Promotable && piece.canPromote()) {
            val position = piece.position
            board[position.y][position.x] = piece.promote()
        }

        return Move(piece, startPosition, endPosition, check, endPiece)
    }

    open fun move(startPosition: Position, endPosition: Position): Move? {
        val piece = getPieceAt(startPosition)
        return if (piece != null) move(piece, endPosition) else null
    }

    fun getPieces(color: ChessPiece.Color? = null): List<ChessPiece> {
        return board.flatten()
            .filterNotNull()
            .filter { color == null || it.color == color }
    }

    fun getOpponentPieces(color: ChessPiece.Color): List<ChessPiece> {
        return board.flatten()
            .filterNotNull()
            .filter { it.color != color }
    }

    fun getPieceAt(x: Int, y: Int): ChessPiece? {
        return if(isValidPosition(x, y)) board[y][x] else null
    }

    fun getPieceAt(position: Position): ChessPiece? {
        return getPieceAt(position.x, position.y)
    }

    fun isPieceAt(x: Int, y: Int): Boolean {
        return getPieceAt(x, y) != null
    }

    fun isPieceAt(position: Position): Boolean {
        return isPieceAt(position.x, position.y)
    }

    fun isBlankAt(x: Int, y: Int): Boolean {
        return !isPieceAt(x, y)
    }

    fun isBlankAt(position: Position): Boolean {
        return !isPieceAt(position)
    }

    fun isAllyAt(x: Int, y: Int, piece: ChessPiece): Boolean {
        return getPieceAt(x, y)?.isAlly(piece) ?: false
    }

    fun isAllyAt(position: Position, piece: ChessPiece): Boolean {
        return isAllyAt(position.x, position.y, piece)
    }

    fun isOpponentAt(x: Int, y: Int, piece: ChessPiece): Boolean {
        val checkingPiece = getPieceAt(x, y) ?: return false
        return checkingPiece.isOpponent(piece)
    }

    fun isOpponentAt(position: Position, piece: ChessPiece): Boolean {
        return isOpponentAt(position.x, position.y, piece)
    }

    fun removePieceAt(position: Position) {
        board[position.y][position.x] = null
    }

    fun syncPieces() {
        // sync: board is right
        for (y in board.indices) {
            for (x in board[y].indices) {
                val piece = getPieceAt(x, y) ?: continue
                val position = piece.position

                // check if it is synced already
                if (position.x == x && position.y == y) {
                    continue
                }

                piece.position = Position.of(x, y)
            }
        }
    }

    fun isHorizontalInBoard(x: Int): Boolean {
        return x in 0 until size
    }

    fun isVerticalInBoard(y: Int): Boolean {
        return y in 0 until size
    }

    fun isValidPosition(x: Int, y: Int): Boolean {
        return isHorizontalInBoard(x) && isVerticalInBoard(y)
    }

    fun queueMoveAction(piece: ChessPiece, position: Position, action: () -> Unit) {
        queuedMoveActions.compute(piece) { _, value ->
            val actions = value ?: mutableSetOf()
            if(actions.none { it.first == position })
                actions.add(position to action)
            return@compute actions
        }
    }

    private fun executeQueuedAction(piece: ChessPiece, endPosition: Position) {
        val actionMovingTo = queuedMoveActions[piece]?.firstOrNull { it.first == endPosition }?.second
        actionMovingTo?.invoke()
        queuedMoveActions.remove(piece)
    }

    override fun toString(): String {
        val builder = StringBuilder()

        for (row in 0 until DEFAULT_SIZE) {
            builder.append("  ")
                .append("+---".repeat(DEFAULT_SIZE) + '+')
                .append("\n")
                .append("$row ")

            for (column in 0 until DEFAULT_SIZE) {
                builder.append("| ${board[row][column]?.type?.notation ?: " "} ")
            }

            builder.append("|")
                .append("\n")
        }

        builder.append("  ")
            .append("+---".repeat(DEFAULT_SIZE) + '+')
            .append("\n")
            .append("  ")

        for (column in 0 until DEFAULT_SIZE) {
            builder.append(" $column  ")
        }

        return builder.toString()
    }

    abstract fun directionOf(color: ChessPiece.Color): Int

    abstract fun setup()

}