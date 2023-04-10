package de.ceskilia.chess.piece

interface Promotable : ChessPiece {

    fun canPromote(): Boolean

    fun promote(): Creatable

}