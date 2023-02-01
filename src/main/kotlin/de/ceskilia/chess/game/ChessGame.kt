@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.DefaultChessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.windetection.WinDetection

class ChessGame {

    val board = DefaultChessboard()
    val players = listOf(
        Player(this, "", ChessPiece.Color.WHITE),
        Player(this, "", ChessPiece.Color.BLACK)
    )

    var currentTurn: Player = players.first()
        private set

    private val winDetection = WinDetection(this)

    init {
        board.setup()
    }

    fun start() {
        while (true) {

            println(board.printBoard())
            println()
            println("Enter new position coordinates:")
            println(currentTurn.calculateMoves().map { it.key.type.notation + "-" + it.value })
            val input = readln().chunked(1) { it.first().digitToInt() }
            val start = Position.of(input[0], input[1])
            val end = Position.of(input[2], input[3])
            val piece = board.getPieceAt(start)

            if (piece == null) {
                println("No Piece")
                continue
            }

            if (!currentTurn.isOwnPiece(piece)) {
                println("Wrong color")
                continue
            }

            val move = currentTurn.move(piece, end)

            if (move == null) {
                println("Cant move there")
                continue
            }

            shuffleTurn()

            // if check, check if mate
            if (move.type.isCheck()) {
                val winningColor = winDetection.isWon() ?: continue

                //at the end, end the game
                break
            }

            // always check for a draw
            val draw = winDetection.checkDraw() ?: continue

            break
        }
    }

    private fun shuffleTurn() {
        val currentIndex = players.indexOf(currentTurn)
        this.currentTurn = players[
            if (currentIndex == players.lastIndex) 0
            else currentIndex + 1
        ]
    }

}