@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.ChessPiece
import de.ceskilia.chess.game.Move
import de.ceskilia.chess.game.Position

abstract class Chessboard(val size: Int = DEFAULT_SIZE) {

    companion object {

        const val DEFAULT_SIZE = 8

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

        var moveType = Move.Type.NORMAL
        val endPiece = board[endPosition.x][endPosition.y]
        val capture = endPiece != null
        val check = false // check if it is checking

        if(capture) {
            capturedPieces.add(endPiece!!)
            moveType = Move.Type.CAPTURE
        }

        if(check) {
            moveType = if(moveType == Move.Type.CAPTURE) Move.Type.CAPTURE_CHECK else Move.Type.CHECK
        }

        board[endPosition.x][endPosition.y] = piece
        board[piece.position.x][piece.position.y] = null

        moves.add(Move(piece, endPosition, moveType))
        piece.position = endPosition
        return moveType
    }

    fun move(startPosition: Position, endPosition: Position): Move.Type {
        val piece = getPieceAt(startPosition)
        return if(piece != null) move(piece, endPosition) else Move.Type.NO_PIECE_AT_POSITION
    }

    fun getPieceAt(x: Int, y: Int): ChessPiece? {
        return board[x][y]
    }

    fun isPieceAt(x: Int, y: Int): Boolean {
        return board[x][y] != null
    }

    fun isBlank(x: Int, y: Int): Boolean {
        return !isPieceAt(x, y)
    }

    fun getPieceAt(position: Position): ChessPiece? {
        return board[position.x][position.y]
    }

    fun removePieceAt(position: Position) {
        board[position.x][position.y] = null
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