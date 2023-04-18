package de.ceskilia.chess.piece.variations

import de.ceskilia.chess.piece.ChessPiece

interface Promotable : ChessPiece {

    fun canPromote(): Boolean

    fun promote(): Creatable

}