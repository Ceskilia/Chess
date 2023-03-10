package de.ceskilia.chess.game.piece.impl

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.piece.ChessPiece

abstract class DefaultChessPiece(
    override val board: Chessboard,
    override val color: ChessPiece.Color,
    override val type: ChessPiece.Type
) : ChessPiece {

    companion object {

        val DEFAULT_POSITION = Position.of(0, 0)

    }

    final override var position = DEFAULT_POSITION
        set(value) {
            if (!this::startPosition.isInitialized)
                this.startPosition = value
            field = value
        }

    private lateinit var startPosition: Position

    override fun hasMoved(): Boolean {
        return this::startPosition.isInitialized && startPosition != position
    }

    fun <T : DefaultChessPiece> T.withPosition(position: Position): T {
        this.position = position
        return this
    }

}