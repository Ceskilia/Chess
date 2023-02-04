package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateArithmeticMoves

class Bishop(
    board: Chessboard,
    position: Position,
    color: Color
) : ChessPiece(board, position, color, Type.BISHOP), Blockable {

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateArithmeticMoves(Operation.DECREMENT, Operation.DECREMENT),
            calculateArithmeticMoves(Operation.DECREMENT, Operation.INCREMENT),
            calculateArithmeticMoves(Operation.INCREMENT, Operation.DECREMENT),
            calculateArithmeticMoves(Operation.INCREMENT, Operation.INCREMENT)
        )
    }

}