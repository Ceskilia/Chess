package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

class Bishop(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.BISHOP) {

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()
        addDiagonalMoves(moves, Operation.DECREMENT, Operation.DECREMENT)
        addDiagonalMoves(moves, Operation.DECREMENT, Operation.INCREMENT)
        addDiagonalMoves(moves, Operation.INCREMENT, Operation.DECREMENT)
        addDiagonalMoves(moves, Operation.INCREMENT, Operation.INCREMENT)
        return moves
    }

    private fun addDiagonalMoves(moves: MutableSet<Position>, xOperation: Operation, yOperation: Operation) {
        var x = position.x
        var y = position.y

        while(true) {
            x += xOperation.operand
            y += yOperation.operand

            if(!board.isHorizontalInBoard(x) || !board.isVerticalInBoard(y)) {
                break
            }

            val piece = board.getPieceAt(x, y)

            if(piece != null) {

                if(isOpponent(piece)) {
                    moves.add(Position.of(x, y))
                }

                break
            }

            moves.add(Position.of(x, y))
        }

    }

    private enum class Operation(val operand: Int) {

        INCREMENT(1),
        DECREMENT(-1)

    }

}