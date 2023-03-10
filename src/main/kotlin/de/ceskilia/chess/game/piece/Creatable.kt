package de.ceskilia.chess.game.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Creatable : ChessPiece {

    fun createCopyAt(position: Position): Creatable

}