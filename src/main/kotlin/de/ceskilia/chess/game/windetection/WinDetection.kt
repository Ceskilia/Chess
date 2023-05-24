package de.ceskilia.chess.game.windetection

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.result.Win

interface WinDetection {

    val game: ChessGame

    fun checkCheckmate(): List<Player>

    fun checkDraw(): Draw.Type?

    fun checkTimeOf(player: Player) {

        if (player.timer.hasTime()) {
            return
        }

        with(game) {

            if (activePlayers.size == 2 && activePlayers.contains(player)) {

                val other = activePlayers.first { it != player }

                // this check is just enough, because we are checking for sufficient material every move
                // for consistency, this might needs to be implemented properly (e.x.: hasSufficientMaterial)
                if (!other.hasPieces()) {
                    finish(Draw(Draw.Type.INSUFFICIENT_MATERIAL_TIMEOUT))
                    return
                }

            }

            removePlayer(player, Win.Reason.TIME)
        }
    }

}