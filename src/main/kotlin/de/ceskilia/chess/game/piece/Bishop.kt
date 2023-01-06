package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.addMoves

class Bishop(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.BISHOP) {

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()
        addMoves(moves, Operation.DECREMENT, Operation.DECREMENT)
        addMoves(moves, Operation.DECREMENT, Operation.INCREMENT)
        addMoves(moves, Operation.INCREMENT, Operation.DECREMENT)
        addMoves(moves, Operation.INCREMENT, Operation.INCREMENT)
        return moves
    }

}