package de.ceskilia.chess.game

import de.ceskilia.chess.game.board.DefaultChessboard
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.windetection.WinDetection

class ChessGame {

    val chessboard = DefaultChessboard()
    val firstPlayer = Player("", ChessPiece.Color.WHITE)
    val secondPlayer = Player("", ChessPiece.Color.BLACK)

    private val winDetection = WinDetection(this)

    init {
        chessboard.setup()
    }

    fun start() {

        while (true) {

            // todo: get input from user
            val startPosition = Position(-1, -1)
            val endPosition = Position(-1, -1)
            val moveType = chessboard.move(startPosition, endPosition)

            // if check, check for winning
            if (moveType.isCheck()) {
                val winningColor = winDetection.isWon() ?: continue


                //at the end, end the game
                break
            }

            // always check for a draw
            if (winDetection.isDraw()) {
                break
            }

        }

    }

}