package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.Blockable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.DefaultChessPiece
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateArithmeticMoves

class Queen(
    board: Chessboard,
    position: Position,
    color: ChessPiece.Color
) : DefaultChessPiece(board, position, color, ChessPiece.Type.QUEEN), Blockable {

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateArithmeticMoves(Operation.DECREMENT, Operation.DECREMENT),
            calculateArithmeticMoves(Operation.DECREMENT, Operation.INCREMENT),
            calculateArithmeticMoves(Operation.INCREMENT, Operation.DECREMENT),
            calculateArithmeticMoves(Operation.INCREMENT, Operation.INCREMENT),
            calculateArithmeticMoves(xOperation = Operation.DECREMENT),
            calculateArithmeticMoves(xOperation = Operation.INCREMENT),
            calculateArithmeticMoves(yOperation = Operation.DECREMENT),
            calculateArithmeticMoves(yOperation = Operation.INCREMENT)
        )
    }

}