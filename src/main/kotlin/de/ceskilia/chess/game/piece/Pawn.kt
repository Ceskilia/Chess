package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.Position
import de.ceskilia.chess.game.board.Chessboard
import kotlin.math.abs

class Pawn(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.PAWN) {

    private val startPosition = position

    override fun calculateMoves(): List<Position> {
        val moves = mutableListOf<Position>()
        val front = position.x + board.directionOf(color)

        // check for normal moves
        if (board.isBlank(front, position.y)) {
            moves.add(position.copy(x = front))

            val further = front + board.directionOf(color)

            if (!moved() && board.isBlank(front, position.y)) {
                moves.add(position.copy(x = further))
            }

        }

        val right = position.y + 1
        val left = position.y - 1

        // check if right can be captured
        if (isInBoard(right) && board.isPieceAt(front, right)) {
            moves.add(position.copy(x = front, y = right))
        }

        // check if left can be captured
        if (isInBoard(left) && board.isPieceAt(front, left)) {
            moves.add(position.copy(x = front, y = left))
        }

        val lastMove = board.lastMove() ?: return moves
        val lastPosition = lastMove.newPosition

        // check for en passant
        if (lastMove.chessPiece.type == Type.PAWN && position.x == lastPosition.x) {

            val yDifference = position.compareTo(lastPosition).y

            // the pawns need to stand next to each other
            if (abs(yDifference) != 1) {
                return moves
            }

            board.removePieceAt(lastPosition)
            moves.add(position.copy(x = front, y = position.y + yDifference))
        }

        return moves
    }

    private fun moved(): Boolean = position != startPosition

    private fun isInBoard(y: Int): Boolean {
        return y in 0..board.size
    }

}