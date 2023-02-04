package de.ceskilia.chess.util

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece

fun ChessPiece.calculateArithmeticMoves(
    xOperation: Operation? = null,
    yOperation: Operation? = null
): Set<Position> {
    val moves = mutableSetOf<Position>()
    var x = position.x
    var y = position.y

    while (true) {

        if (xOperation != null) {
            x += xOperation.operand
        }

        if (yOperation != null) {
            y += yOperation.operand
        }

        if (!board.isValidPosition(x, y)) {
            break
        }

        val piece = board.getPieceAt(x, y)

        // temporary solution
        if (piece != null) {

            if (isOpponent(piece) && piece.type != ChessPiece.Type.KING) {
                moves.add(Position.of(x, y))
            }

            break
        }

        moves.add(Position.of(x, y))
    }

    return moves
}

enum class Operation(val operand: Int) {

    INCREMENT(1),
    DECREMENT(-1)

}