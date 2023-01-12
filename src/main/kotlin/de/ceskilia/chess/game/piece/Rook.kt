package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.addMoves

class Rook(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.ROOK) {

    override fun calculateCoveringMoves(): Set<Position> {
        val moves = mutableSetOf<Position>()
        addMoves(moves, xOperation = Operation.DECREMENT)
        addMoves(moves, xOperation = Operation.INCREMENT)
        addMoves(moves, yOperation = Operation.DECREMENT)
        addMoves(moves, yOperation = Operation.INCREMENT)
        return moves
    }

}