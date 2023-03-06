package de.ceskilia.chess.game.board

import de.ceskilia.chess.game.arithmetic.Position

internal data class Action(val position: Position, val cancel: Boolean, val action: (Position) -> Unit)
