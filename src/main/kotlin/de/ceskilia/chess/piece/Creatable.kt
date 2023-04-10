package de.ceskilia.chess.piece

import de.ceskilia.chess.game.arithmetic.Position

interface Creatable : ChessPiece {

    fun createCopyAt(position: Position): Creatable

}