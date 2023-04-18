package de.ceskilia.chess.piece.variations

import de.ceskilia.chess.piece.ChessPiece

interface Checkable : ChessPiece {

    fun isChecked(): Boolean {
        return board.opponentPieces(color)
            .filter { it::class != this::class }
            .any {
                it.canMoveTo(this.position)
            }
    }

}