package de.ceskilia.chess.util

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.internal.Action
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.annotation.Interchangeable
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Blockable
import de.ceskilia.chess.piece.variation.Checkable
import kotlin.math.abs

fun AbstractKing.checkCastleDirection(side: Chessboard.Side): Set<Position> {
    val line = calculateInterchangeableLine(side)

    if (line.size != 1) {
        return emptySet()
    }

    val interchangeable = line.first()

    if (interchangeable.hasMoved()) {
        return emptySet()
    }

    val direction = side.direction
    val newInterchangeablePosition = this.position.copyAdding(x = direction)
    val endPosition = this.position.copyAdding(x = 2 * direction)
    val isBlocked = newInterchangeablePosition.isCoveredByOpponent() || endPosition.isCoveredByOpponent()

    if (isBlocked) {
        return emptySet()
    }

    val interchangeableMove = interchangeable.position.whenOccupying(result = Action.Result.CANCEL) {
        board.moveUnchecked(interchangeable, newInterchangeablePosition)
        board.moveUnchecked(this, endPosition)
    }
    val kingMove = endPosition.whenOccupying {
        board.moveUnchecked(interchangeable, newInterchangeablePosition)
    }

    return setOf(interchangeableMove, kingMove)
}

private fun AbstractKing.calculateInterchangeableLine(
    side: Chessboard.Side,
    restricted: Boolean = true
): List<ChessPiece> {
    val moves = if (restricted)
        calculateCoveringArithmeticMoves(CoordinateOperation.fromDirection(side.direction))
    else
        calculateMoves(
            startMoves = mutableSetOf(),
            startPosition = this.position,
            xOperation = CoordinateOperation.fromDirection(side.direction)
        )

    return moves
        .mapNotNull { board.pieceAt(it) }
        .filter { it.color == this.color }
        .filter { it.javaClass.isAnnotationPresent(Interchangeable::class.java) }
}

fun ChessPiece.calculateExtendedMovesPinned(): Set<Position> {
    val moves = calculateMovesUnpinned()
    val pinningPiece = pinningPiece() ?: return moves

    return pinningPiece.calculatePinLine(this)!!
        .plus(pinningPiece.position) // the piece could be able to capture the pinning piece
        .filter { moves.contains(it) }
        .toSet()
}

fun Blockable.addPinMoves(line: Set<Position>): Set<Position> {
    val startMoves = line.toMutableSet()
    val lastPosition = startMoves.firstOrNull { board.isOpponentAt(it, this) } ?: return startMoves

    return calculateMoves(
        startMoves = startMoves,
        startPosition = lastPosition,
        xOperation = CoordinateOperation.fromDirection(lastPosition.x - position.x),
        yOperation = CoordinateOperation.fromDirection(lastPosition.y - position.y)
    ) { piece, moves ->
        if (isAlly(piece)) {
            return@calculateMoves true
        }

        if (piece is Checkable) {
            moves.add(piece.position)
        }

        true
    }
}

fun ChessPiece.calculateCoveringArithmeticMoves(
    xOperation: CoordinateOperation = CoordinateOperation.CONSTANT,
    yOperation: CoordinateOperation = CoordinateOperation.CONSTANT
): Set<Position> {
    return calculateMoves(
        startMoves = mutableSetOf(),
        startPosition = this.position,
        xOperation = xOperation,
        yOperation = yOperation
    ) { piece, moves ->
        moves.add(piece.position)

        if (isAlly(piece)) {
            return@calculateMoves true
        }

        piece !is Checkable
    }
}

private fun ChessPiece.calculateMoves(
    startMoves: MutableSet<Position>,
    startPosition: Position,
    xOperation: CoordinateOperation = CoordinateOperation.CONSTANT,
    yOperation: CoordinateOperation = CoordinateOperation.CONSTANT,
    condition: ((ChessPiece, MutableSet<Position>) -> Boolean) = { _, _ -> false }
): Set<Position> {
    var x = startPosition.x
    var y = startPosition.y

    while (true) {
        x += xOperation.direction
        y += yOperation.direction

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

private fun Player.hasCastleRight(side: Chessboard.Side): Boolean {
    val king = ownKing()

    if (king.hasMoved()) {
        return false
    }

    val line = king.calculateInterchangeableLine(side = side, restricted = false)

    if (line.size != 1) {
        return false
    }

    val interchangeable = line.first()
    return !interchangeable.hasMoved()
}

fun calculateUniqueFEN(game: ChessGame): String {
    val position = calculatePositionFEN(game)
    val moveRight = currentMoveRight(game)
    val castles = castleRights(game)

    return "$position $moveRight $castles"
}

fun calculateFEN(game: ChessGame): String {
    val uniqueFEN = calculateUniqueFEN(game)
    val enPassants = enPassantRight(game)
    val insignificantMoves = countInsignificantMoves(game)
    val nextMove = nextMoveCount(game)

    return "$uniqueFEN $enPassants $insignificantMoves $nextMove"
}

private fun calculatePositionFEN(game: ChessGame): String {
    with(game.board) {
        val fen = StringBuilder()

        for (y in 0 until size) {
            fen.appendIf('/') { y > 0 }

            var counter = 0

            for (x in 0 until size) {
                val piece = pieceAt(x, y)

                if (piece == null) {
                    counter++
                    continue
                }

                if (counter > 0) {
                    fen.append(counter)
                    counter = 0
                }

                fen.append(
                    formatWithColor(
                        if (piece is AbstractPawn) 'P' else piece.notation,
                        piece.color
                    )
                )
            }

            fen.appendIf(counter) { it > 0 }
        }

        return fen.toString()
    }
}

private fun currentMoveRight(game: ChessGame): String {
    return game.currentTurn.pieceColor.name
        .first()
        .lowercase()
}

private fun castleRights(game: ChessGame): String {
    val result = StringBuilder()

    for (player in game.activePlayers) {
        val color = player.pieceColor

        if (player.hasCastleRight(Chessboard.Side.KING_SIDE))
            result.append(formatWithColor('K', color))
        if (player.hasCastleRight(Chessboard.Side.QUEEN_SIDE))
            result.append(formatWithColor('Q', color))
    }

    return result.toString()
        .ifEmpty { "-" }
}

private fun enPassantRight(game: ChessGame): String {
    val board = game.board
    val lastMove = game.history.lastMove() ?: return "-"
    val lastChessPiece = lastMove.chessPiece

    if (lastChessPiece is AbstractPawn) {
        val yDifference = abs(lastMove.startPosition.y - lastMove.endPosition.y)

        if (yDifference == 2) {
            val direction = board.directionOf(lastChessPiece)

            return lastMove.startPosition.copyAdding(y = direction)
                .toString()
        }

    }

    return "-"
}

private fun countInsignificantMoves(game: ChessGame): String {
    var insignificantMoves = 0

    for (move in game.history.moves) {

        if (move.chessPiece is AbstractPawn || move.isCapture) {
            insignificantMoves = 0
            continue
        }

        insignificantMoves++
    }

    return insignificantMoves.toString()
}

private fun nextMoveCount(game: ChessGame): String {
    return game.history.nextMoveCount()
        .toString()
}

private fun formatWithColor(value: Char, color: ChessPiece.Color): Char {
    return when (color) {
        ChessPiece.Color.WHITE -> value.uppercaseChar()
        ChessPiece.Color.BLACK -> value.lowercaseChar()
    }
}