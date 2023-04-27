package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.piece.variation.Blockable
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.util.CoordinateOperation
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

@Valuable(9u)
class Queen(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), Blockable, Creatable {

    override val notation: Char = 'Q'

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateCoveringArithmeticMoves(CoordinateOperation.DECREMENT, CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.DECREMENT, CoordinateOperation.INCREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.INCREMENT, CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.INCREMENT, CoordinateOperation.INCREMENT),
            calculateCoveringArithmeticMoves(xOperation = CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(xOperation = CoordinateOperation.INCREMENT),
            calculateCoveringArithmeticMoves(yOperation = CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(yOperation = CoordinateOperation.INCREMENT)
        )
    }

    override fun createCopyAt(position: Position): Creatable {
        return Queen(this.board, this.color).withPosition(position)
    }

}