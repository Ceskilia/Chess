package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.piece.ChessPiece

interface Creatable : ChessPiece {

    fun createCopy(): Creatable

}