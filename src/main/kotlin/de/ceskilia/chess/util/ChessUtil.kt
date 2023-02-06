package de.ceskilia.chess.util

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.BlockableChessPiece
import de.ceskilia.chess.game.piece.ChessPiece

fun BlockableChessPiece.calculateArithmeticMoves(
    xOperation: Operation? = null,
    yOperation: Operation? = null
): Set<Position> {
    return calculateMoves(
        startMoves = mutableSetOf(),
        startPosition = position,
        xOperation = xOperation,
        yOperation = yOperation
    ) { piece, moves ->
        if (isOpponent(piece) && piece.type != ChessPiece.Type.KING) {
            moves.add(piece.position)
        }

        true
    }
}

fun BlockableChessPiece.calculatePinMoves(line: Set<Position>): Set<Position> {
    val startMoves = line.toMutableSet()
    val lastPosition = startMoves.firstOrNull { board.isOpponentAt(it, this) } ?: return startMoves

    return calculateMoves(
        startMoves = startMoves,
        startPosition = lastPosition,
        xOperation = Operation.fromOperand(lastPosition.x - position.x),
        yOperation = Operation.fromOperand(lastPosition.y - position.y)
    ) { piece, moves ->
        if (isAlly(piece)) {
            return@calculateMoves true
        }

        if (piece.type == ChessPiece.Type.KING) {
            moves.add(piece.position)
            return@calculateMoves true
        }

        moves.any { board.isPieceAt(it) }
    }
}

private fun BlockableChessPiece.calculateMoves(
    startMoves: MutableSet<Position>,
    startPosition: Position,
    xOperation: Operation? = null,
    yOperation: Operation? = null,
    condition: (ChessPiece, MutableSet<Position>) -> Boolean
): Set<Position> {
    var x = startPosition.x
    var y = startPosition.y

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

        if (piece != null && condition(piece, startMoves)) {
            break
        }

        startMoves.add(Position.of(x, y))
    }

    return startMoves
}

enum class Operation(val operand: Int) {

    INCREMENT(1),
    DECREMENT(-1);

    companion object {

        fun fromOperand(delta: Int): Operation? {
            return when {
                delta > 0 -> INCREMENT
                delta < 0 -> DECREMENT
                else -> null
            }
        }

    }

}