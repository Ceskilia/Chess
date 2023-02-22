package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.Blockable
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.Creatable
import de.ceskilia.chess.game.piece.DefaultChessPiece
import de.ceskilia.chess.util.Operation
import de.ceskilia.chess.util.calculateArithmeticMoves

class Rook(
    board: Chessboard,
    position: Position,
    color: ChessPiece.Color
) : DefaultChessPiece(board, position, color, ChessPiece.Type.ROOK), Blockable, Creatable {

    override fun calculateLines(): List<Set<Position>> {
        return listOf(
            calculateArithmeticMoves(xOperation = Operation.DECREMENT),
            calculateArithmeticMoves(xOperation = Operation.INCREMENT),
            calculateArithmeticMoves(yOperation = Operation.DECREMENT),
            calculateArithmeticMoves(yOperation = Operation.INCREMENT)
        )
    }

    override fun createCopyAt(position: Position): Creatable {
        return Rook(this.board, position, this.color)
    }

}