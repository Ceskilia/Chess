package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard

abstract class DefaultChessPiece(
    final override val board: Chessboard,
    final override var position: Position,
    final override val color: ChessPiece.Color,
    final override val type: ChessPiece.Type
) : ChessPiece {

    private val startPosition = position

    override fun hasMoved(): Boolean {
        return startPosition != position
    }

}