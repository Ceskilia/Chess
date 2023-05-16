package de.ceskilia.chess.game.result

import de.ceskilia.chess.game.player.Player

class Win(val player: Player, val reason: Reason) : GameResult {

    enum class Reason {

        TIME,
        RESIGNATION,
        CHECKMATE

    }

}