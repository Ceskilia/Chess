package de.ceskilia.chess.piece.variation

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.piece.ChessPiece

interface Promotable : ChessPiece {

    fun canPromote(): Boolean

    fun promote(): Context

    data class Context(val creatable: Creatable, val position: Position)

}