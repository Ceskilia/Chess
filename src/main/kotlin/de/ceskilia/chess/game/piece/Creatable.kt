package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Creatable : ChessPiece {

    fun withPosition(position: Position): Creatable {
        this.position = position
        return this
    }

    fun createCopyAt(position: Position): Creatable

}