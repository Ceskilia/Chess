package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.Blockable
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.Creatable
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

@Valuable(9u)
class Queen(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), Blockable, Creatable {

    override val notation: Char = 'Q'

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateCoveringArithmeticMoves(Operation.DECREMENT, Operation.DECREMENT),
            calculateCoveringArithmeticMoves(Operation.DECREMENT, Operation.INCREMENT),
            calculateCoveringArithmeticMoves(Operation.INCREMENT, Operation.DECREMENT),
            calculateCoveringArithmeticMoves(Operation.INCREMENT, Operation.INCREMENT),
            calculateCoveringArithmeticMoves(xOperation = Operation.DECREMENT),
            calculateCoveringArithmeticMoves(xOperation = Operation.INCREMENT),
            calculateCoveringArithmeticMoves(yOperation = Operation.DECREMENT),
            calculateCoveringArithmeticMoves(yOperation = Operation.INCREMENT)
        )
    }

    override fun createCopyAt(position: Position): Creatable {
        return Queen(this.board, this.color).withPosition(position)
    }

}