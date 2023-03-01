package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.Blockable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateCoveringArithmeticMoves

class Bishop(
    board: Chessboard,
    color: ChessPiece.Color
) : DefaultChessPiece(board, color, ChessPiece.Type.BISHOP), Blockable, Creatable {

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateCoveringArithmeticMoves(Operation.DECREMENT, Operation.DECREMENT),
            calculateCoveringArithmeticMoves(Operation.DECREMENT, Operation.INCREMENT),
            calculateCoveringArithmeticMoves(Operation.INCREMENT, Operation.DECREMENT),
            calculateCoveringArithmeticMoves(Operation.INCREMENT, Operation.INCREMENT)
        )
    }

    override fun createCopyAt(position: Position): Creatable {
        return Bishop(this.board, this.color).withPosition(position)
    }

}