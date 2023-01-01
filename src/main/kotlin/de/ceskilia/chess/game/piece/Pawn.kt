package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import kotlin.math.abs

class Pawn(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.PAWN) {

    private val startPosition = position

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()
        val oneStep = position.x + board.directionOf(color)
        val right = position.y + 1
        val left = position.y - 1

        // check if right can be captured
        if (board.isVerticalInBoard(right) && board.isOpponentAt(oneStep, right, this)) {
            moves.add(Position.of(oneStep, right))
        }

        // check if left can be captured
        if (board.isVerticalInBoard(left) && board.isOpponentAt(oneStep, left, this)) {
            moves.add(Position.of(oneStep, left))
        }

        val lastMove = board.lastMove() ?: return moves
        val lastPosition = lastMove.newPosition

        // if the last move somehow was done by the same color -> don't check for en passant
        if (lastMove.chessPiece.color == color) {
            return moves
        }

        // check for en passant
        if (lastMove.chessPiece.type == Type.PAWN && position.x == lastPosition.x) {

            val yDifference = position.compareTo(lastPosition).y

            // the pawns need to stand next to each other
            if (abs(yDifference) != 1) {
                return moves
            }

            board.removePieceAt(lastPosition)
            moves.add(Position.of(oneStep, position.y + yDifference))
        }

        return moves
    }

    override fun calculateMoves(): Set<Position> {
        val moves = calculateCoveringMoves().toMutableSet()
        val oneStep = position.x + board.directionOf(color)

        // check for normal moves
        if (board.isBlankAt(oneStep, position.y)) {
            moves.add(position.copy(x = oneStep))

            val twoSteps = oneStep + board.directionOf(color)

            if (!moved() && board.isBlankAt(oneStep, position.y)) {
                moves.add(position.copy(x = twoSteps))
            }

        }

        return moves
    }

    private fun moved(): Boolean = position != startPosition

}