package de.ceskilia.chess.game.windetection

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.ChessPiece

class WinDetection(val game: ChessGame) {

    fun isWon(): ChessPiece.Color? {
        return null
    }

    fun isDraw(): Boolean {
        return false
    }

}