@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.util.notNegative

abstract class Chessboard(val size: Int = DEFAULT_SIZE) {

    companion object {

        const val DEFAULT_SIZE = 8

    }

    init {
        notNegative(size) { "Size may not be negative: size=$size" }
    }

    val moves = mutableListOf<Move>()
    val capturedPieces = mutableListOf<ChessPiece>()

    protected val board: List<MutableList<ChessPiece?>> = List(size) {
        MutableList(size) { null }
    }

    fun hasLastMove(): Boolean = moves.isNotEmpty()

    fun lastMove(): Move? = if (hasLastMove()) moves.last() else null

    // TODO: CHECKS
    fun move(piece: ChessPiece, endPosition: Position): Move.Type {

        if (!piece.canMoveTo(endPosition)) {
            return Move.Type.INVALID_END_POSITION
        }

        val startPosition = piece.position
        piece.position = endPosition

        var moveType = Move.Type.NORMAL
        val endPiece = board[endPosition.x][endPosition.y]
        val capture = endPiece != null
        val check = piece.canMoveTo(getPieces()
            .filter { it.color != piece.color }
            .first { it.type == ChessPiece.Type.KING }
            .position
        )

        if (capture) {
            capturedPieces.add(endPiece!!)
            moveType = Move.Type.CAPTURE
        }

        if (check) {
            moveType = if (moveType == Move.Type.CAPTURE) Move.Type.CAPTURE_CHECK else Move.Type.CHECK
        }

        removePieceAt(startPosition)
        board[endPosition.x][endPosition.y] = piece
        moves.add(Move(piece, startPosition, endPosition, moveType))

        return moveType
    }

    fun move(startPosition: Position, endPosition: Position): Move.Type {
        val piece = getPieceAt(startPosition)
        return if (piece != null) move(piece, endPosition) else Move.Type.NO_PIECE_AT_POSITION
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

    fun isBlankAt(x: Int, y: Int): Boolean {
        return !isPieceAt(x, y)
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