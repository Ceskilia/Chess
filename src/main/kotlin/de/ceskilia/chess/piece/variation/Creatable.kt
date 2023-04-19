package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

interface Creatable : ChessPiece {

    fun createCopyAt(position: Position): Creatable

}