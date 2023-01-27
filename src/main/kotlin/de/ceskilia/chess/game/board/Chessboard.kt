@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.BoardHistory
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.util.notNegative

abstract class Chessboard(val size: Int = DEFAULT_SIZE) {

    companion object {

        const val DEFAULT_SIZE = 8

    }

    init {
        notNegative(size) { "Size may not be negative. Provided: $size" }
    }

    val history = BoardHistory()

    protected val board: List<MutableList<ChessPiece?>> = List(size) {
        MutableList(size) { null }
    }

    // TODO: CHECKS
    fun move(piece: ChessPiece, endPosition: Position): Move? {

        if (!piece.canMoveTo(endPosition)) {
            return null
        }

        val startPosition = piece.position
        val endPiece = board[endPosition.x][endPosition.y]

        piece.position = endPosition
        board[endPosition.x][endPosition.y] = piece
        removePieceAt(startPosition)

        var moveType = Move.Type.NORMAL
        val capture = endPiece != null
        val check = getPieces(piece.color).any { ally ->
            ally.canMoveTo(getPieces()
                .filter { it.color != piece.color }
                .first { it.type == ChessPiece.Type.KING }
                .position
            )
        }

        if (capture) {
            history.capturedPieces.add(endPiece!!)
            moveType = Move.Type.CAPTURE
        }

        if (check) {
            moveType = if (moveType == Move.Type.CAPTURE) Move.Type.CAPTURE_CHECK else Move.Type.CHECK
        }

        val move = Move(piece, startPosition, endPosition, moveType)
        history.moves.add(move)
        return move
    }

    fun move(startPosition: Position, endPosition: Position): Move? {
        val piece = getPieceAt(startPosition)
        return if (piece != null) move(piece, endPosition) else null
    }

    fun getPieces(color: ChessPiece.Color? = null): List<ChessPiece> {
        return board.flatten()
            .filterNotNull()
            .filter { color == null || it.color == color }
    }

    fun getPieceAt(x: Int, y: Int): ChessPiece? {
        return board[x][y]
    }

    fun getPieceAt(position: Position): ChessPiece? {
        return board[position.x][position.y]
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
        board[position.x][position.y] = null
    }

    fun syncPieces() {
        // sync: board is right
        for (x in board.indices) {
            for (y in board[x].indices) {
                val piece = board[x][y] ?: continue

                // check if it is synced already
                if (piece.position.x == x && piece.position.y == y) {
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

    fun printBoard(): String {
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