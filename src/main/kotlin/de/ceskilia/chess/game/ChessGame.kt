@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.DefaultChessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.result.GameResult
import de.ceskilia.chess.game.result.Win
import de.ceskilia.chess.game.windetection.WinDetection

class ChessGame {

    val history = GameHistory()
    val board = DefaultChessboard(history)
    val players = listOf(
        Player(this, "", ChessPiece.Color.WHITE),
        Player(this, "", ChessPiece.Color.BLACK)
    )

    var result: GameResult<*>? = null
        private set
    var currentTurn: Player = players.first()
        private set

    private val winDetection = WinDetection(this)

    init {
        board.setup()
    }

    fun finish(result: GameResult<*>) {
        if(this.result != null)
            return
        this.result = result
    }

    fun isFinished(): Boolean = result != null

    fun shuffleTurn() {
        val currentIndex = players.indexOf(currentTurn)
        this.currentTurn = players[
            if (currentIndex == players.lastIndex) 0
            else currentIndex + 1
        ]
    }

    fun start() {
        while (!isFinished()) {

            println(board.printBoard())
            println()
            println("Enter new position coordinates:")
            println(currentTurn.calculateMoves().map { it.key.type.notation + "-" + it.value })
            val (start, end) = readln().chunked(2).map(Position.Companion::fromNotation)
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
            if (move.isCheck) {
                val winningPlayer = winDetection.isWon() ?: continue

                finish(Win(winningPlayer))
                //at the end, end the game
                break
            }

            // always check for a draw
            val drawType = winDetection.checkDraw() ?: continue

            finish(Draw(drawType))
            break
        }
    }

}