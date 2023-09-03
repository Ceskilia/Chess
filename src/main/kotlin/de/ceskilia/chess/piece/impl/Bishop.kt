package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Valuable
import de.ceskilia.chess.piece.variation.Blockable
import de.ceskilia.chess.piece.variation.Creatable
import de.ceskilia.chess.util.CoordinateOperation
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

@Valuable(3)
class Bishop(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color), Blockable, Creatable {

    override val notation: Char = 'B'

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateCoveringArithmeticMoves(CoordinateOperation.DECREMENT, CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.DECREMENT, CoordinateOperation.INCREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.INCREMENT, CoordinateOperation.DECREMENT),
            calculateCoveringArithmeticMoves(CoordinateOperation.INCREMENT, CoordinateOperation.INCREMENT)
        )
    }

    override fun createCopy(): Creatable {
        return Bishop(this.board, this.color)
    }

}