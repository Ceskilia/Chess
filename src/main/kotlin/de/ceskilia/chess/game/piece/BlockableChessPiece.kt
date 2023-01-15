package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

abstract class BlockableChessPiece(
    board: Chessboard,
    position: Position,
    color: Color,
    type: Type
) : ChessPiece(board, position, color, type) {

    override fun calculateCoveringMoves(): Set<Position> {
        return calculateLines()
            .flatten()
            .toSet()
    }

    // lines make it possible to calculate check blocking moves
    abstract fun calculateLines(): List<Set<Position>>

}