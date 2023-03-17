package de.ceskilia.chess.game.windetection

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.piece.impl.Bishop
import de.ceskilia.chess.game.piece.impl.Knight
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw

class WinDetection(val game: ChessGame) {

    fun isWon(): Player? {

        game.players.forEach {
            it.opponents().forEach { opponent ->
                if(opponent.isCheckmating(it)) {
                    return opponent
                }
            }
        }

        return null
    }

    fun checkDraw(): Draw.Type? {
        val pieces = game.board.pieces()

        // stalemate
        for (player in game.players) {
            if (player.isStalemated()) {
                return Draw.Type.STALEMATE
            }
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

        val history = game.history

        // there need to be at least 10 elements for threefold repetition/50 moves draw
        if (history.moves.size < 10) {
            return null
        }

        // threefold repetition
        val tableStatesOccurrences = game.history.encodedTableStates.map { state ->
            game.history.encodedTableStates.count { it == state }
        }.distinct()

        // when there are 3 or more positions that are the same -> its three folded
        if (tableStatesOccurrences.any { it >= 3 }) {
            return Draw.Type.THREEFOLD_REPETITION
        }

        // there need to be at least 50 elements for 50 moves draw
        if (history.moves.size < 50) {
            return null
        }

        // 50 moves
        // take last 50 elements
        val lastFiftyMoves = history.lastMoves(50)

        // no pawn move and no capture
        if (lastFiftyMoves.none { it.chessPiece.type == ChessPiece.Type.PAWN }
            && lastFiftyMoves.none(Move::isCapture)) {
            return Draw.Type.FIFTY_MOVES
        }

        return null
    }

}