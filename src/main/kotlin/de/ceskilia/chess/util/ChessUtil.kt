package de.ceskilia.chess.util

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.BlockableChessPiece
import de.ceskilia.chess.game.piece.ChessPiece

// todo: keine extension function
fun BlockableChessPiece.calculateArithmeticMoves(
    xOperation: Operation? = null,
    yOperation: Operation? = null
): Set<Position> {
    val moves = mutableSetOf<Position>()
    var x = position.x
    var y = position.y

    while (true) {

        if(xOperation != null) {
            x += xOperation.operand
        }

        if(yOperation != null) {
            y += yOperation.operand
        }

        if (!board.isHorizontalInBoard(x) || !board.isVerticalInBoard(y)) {
            break
        }

        val piece = board.getPieceAt(x, y)

        // temporary solution
        if (piece != null && piece.type != ChessPiece.Type.KING) {

            if (isOpponent(piece)) {
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