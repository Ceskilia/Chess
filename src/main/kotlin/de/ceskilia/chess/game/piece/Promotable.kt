package de.ceskilia.chess.game.piece

interface Promotable : ChessPiece {

    fun canPromote(): Boolean

    fun promote(): Creatable

}