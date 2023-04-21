package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Interchangeable
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.piece.variation.Blockable
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

@Valuable(5u)
@Interchangeable
class Rook(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), Blockable, Creatable {

    override val notation: Char = 'R'

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateCoveringArithmeticMoves(xOperation = Operation.DECREMENT),
            calculateCoveringArithmeticMoves(xOperation = Operation.INCREMENT),
            calculateCoveringArithmeticMoves(yOperation = Operation.DECREMENT),
            calculateCoveringArithmeticMoves(yOperation = Operation.INCREMENT)
        )
    }

    override fun createCopyAt(position: Position): Creatable {
        return Rook(this.board, this.color).withPosition(position)
    }

}