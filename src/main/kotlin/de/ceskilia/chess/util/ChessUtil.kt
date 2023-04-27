package de.ceskilia.chess.util

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Action
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Interchangeable
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.piece.variation.Blockable

fun AbstractKing.checkCastleDirection(xOperation: CoordinateOperation): Set<Position> {
    val line = calculateCoveringArithmeticMoves(xOperation)
        .mapNotNull { board.pieceAt(it) }
        .filter { it.color == this.color }
        .filter { it.javaClass.isAnnotationPresent(Interchangeable::class.java) }

    if (line.size != 1) {
        return emptySet()
    }

    val interchangeable = line.first()

    if (interchangeable.hasMoved()) {
        return emptySet()
    }

    val direction = xOperation.operand
    val newInterchangeablePosition = this.position.copyAdding(x = direction)
    val endPosition = this.position.copyAdding(x = 2 * direction)
    val isBlocked = newInterchangeablePosition.isCoveredByOpponent() || endPosition.isCoveredByOpponent()

    if (isBlocked) {
        return emptySet()
    }

    val interchangeableMove = interchangeable.position.whenOccupied(result = Action.Result.CANCEL) {
        board.moveUnchecked(interchangeable, newInterchangeablePosition)
        board.moveUnchecked(this, endPosition)
    }
    val kingMove = endPosition.whenOccupied {
        board.moveUnchecked(interchangeable, newInterchangeablePosition)
    }

    return setOf(interchangeableMove, kingMove)
}

fun ChessPiece.calculateExtendedMovesPinned(): Set<Position> {
    val moves = calculateMovesUnpinned()
    val pinningPiece = pinningPiece() ?: return moves

    return pinningPiece.calculatePinLine(this)!!
        .plus(pinningPiece.position)
        .filter { moves.contains(it) }
        .toSet()
}

fun Blockable.addPinMoves(line: Set<Position>): Set<Position> {
    val startMoves = line.toMutableSet()
    val lastPosition = startMoves.firstOrNull { board.isOpponentAt(it, this) } ?: return startMoves

    return calculateMoves(
        startMoves = startMoves,
        startPosition = lastPosition,
        xOperation = CoordinateOperation.fromOperand(lastPosition.x - position.x),
        yOperation = CoordinateOperation.fromOperand(lastPosition.y - position.y)
    ) { piece, moves ->
        if (isAlly(piece)) {
            return@calculateMoves true
        }

        if (piece is AbstractKing) {
            moves.add(piece.position)
        }

        true
    }
}

fun ChessPiece.calculateCoveringArithmeticMoves(
    xOperation: CoordinateOperation? = null,
    yOperation: CoordinateOperation? = null
): Set<Position> {
    return calculateMoves(
        startMoves = mutableSetOf(),
        startPosition = position,
        xOperation = xOperation,
        yOperation = yOperation
    ) { piece, moves ->
        moves.add(piece.position)

        if (isAlly(piece)) {
            return@calculateMoves true
        }

        piece !is AbstractKing
    }
}

private fun ChessPiece.calculateMoves(
    startMoves: MutableSet<Position>,
    startPosition: Position,
    xOperation: CoordinateOperation? = null,
    yOperation: CoordinateOperation? = null,
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

        if (!board.inBounds(x, y)) {
            break
        }

        val piece = board.pieceAt(x, y)

        if (piece != null && condition(piece, startMoves)) {
            break
        }

        startMoves.add(Position.of(x, y))
    }

    return startMoves
}