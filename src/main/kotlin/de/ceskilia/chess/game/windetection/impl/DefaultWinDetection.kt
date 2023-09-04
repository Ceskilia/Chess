package de.ceskilia.chess.game.windetection.impl

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.windetection.WinDetection
import de.ceskilia.chess.piece.impl.Bishop
import de.ceskilia.chess.piece.impl.Knight

class DefaultWinDetection(override val game: ChessGame) : WinDetection {

    override fun checkCheckmate(): List<Player> {
        val players = mutableListOf<Player>()

        game.activePlayers.forEach {
            it.opponents()
                .filter(Player::isActive)
                .forEach { opponent ->
                    if (opponent.isCheckmating(it)) {
                        players.add(it)
                    }
                }
        }

        return players
    }

    override fun checkDraw(): Draw.Type? {
        val pieces = game.board.pieces()

        // stalemate
        if (game.currentTurn.isStalemated()) {
            return Draw.Type.STALEMATE
        }

        // insufficient material
        // king vs king
        // king and bishop vs king
        // king and knight vs king
        // king and bishop vs king and bishop (bishop on the same square color)
        when (pieces.size) {
            2 -> return Draw.Type.INSUFFICIENT_MATERIAL // 2 kings
            3 -> run {
                // 2 kings and 1 other piece
                if (pieces.none { it is Bishop || it is Knight }) {
                    return@run
                }

                return Draw.Type.INSUFFICIENT_MATERIAL
            }

            4 -> run {

                // filter only bishops
                val bishops = pieces.filterIsInstance<Bishop>()

                // the last remaining pieces must be bishops
                if (bishops.size != 2) {
                    return@run
                }

                val (first, second) = bishops

                // the bishops need to have a different color and the square color must be the same
                if (first.color == second.color || first.position.color() != second.position.color()) {
                    return@run
                }

                return Draw.Type.INSUFFICIENT_MATERIAL
            }
        }

        return with(game.history) {
            when {
                hasThreefoldRepetition() -> Draw.Type.THREEFOLD_REPETITION
                hasFiftyInsignificantMoves() -> Draw.Type.FIFTY_MOVES
                else -> null
            }
        }
    }

}