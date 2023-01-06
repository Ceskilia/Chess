package de.ceskilia.chess.util

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.ChessPiece

fun ChessPiece.addMoves(
    moves: MutableSet<Position>,
    xOperation: Operation? = null,
    yOperation: Operation? = null
) {
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

        if (piece != null) {

            if (isOpponent(piece)) {
                moves.add(Position.of(x, y))
            }

            break
        }

        moves.add(Position.of(x, y))
    }

}

enum class Operation(val operand: Int) {

    INCREMENT(1),
    DECREMENT(-1)

}