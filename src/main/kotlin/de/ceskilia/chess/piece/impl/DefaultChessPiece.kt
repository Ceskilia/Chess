package de.ceskilia.chess.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.piece.ChessPiece

/**
 * The default implementation of a [ChessPiece], reducing boilerplate.
 */
abstract class DefaultChessPiece(
    override val board: Chessboard,
    override val color: ChessPiece.Color
) : ChessPiece {

    /**
     * The initial position of this piece. First initialized when the piece is placed on the [board].
     */
    private lateinit var startPosition: Position

    override fun onMove() {
        if (::startPosition.isInitialized)
            return
        this.startPosition = this.position
    }

    override fun hasMoved(): Boolean {
        return this::startPosition.isInitialized && startPosition != position
    }

}