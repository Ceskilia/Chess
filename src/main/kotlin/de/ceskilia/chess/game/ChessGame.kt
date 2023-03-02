@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.result.GameResult
import de.ceskilia.chess.game.result.MoveResult
import de.ceskilia.chess.game.result.Win
import de.ceskilia.chess.game.windetection.WinDetection

abstract class ChessGame {

    private val moves = mutableListOf<Move>()
    protected val winDetection by lazy { WinDetection(this) }

    val history = GameHistory(moves)

    abstract val players: List<Player>
    abstract val board: Chessboard

    var result: GameResult<*>? = null
        private set
    lateinit var currentTurn: Player
        private set

    init {
        lazy {
            currentTurn = players.first()
            board.setup()
        }
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

    open fun move(startPosition: Position, endPosition: Position): MoveResult {
        val piece = board.pieceAt(startPosition) ?: return MoveResult.INVALID_POSITION

        if (!currentTurn.isOwnPiece(piece)) {
            return MoveResult.WRONG_COLOR
        }

        val move = currentTurn.move(piece, endPosition) ?: return MoveResult.INVALID_MOVE
        moves.add(move)
        shuffleTurn()

        // if check, check if mate
        if (move.isCheck) {
            val winningPlayer = winDetection.isWon()

            if(winningPlayer != null) {
                finish(Win(winningPlayer))
            }

            return MoveResult.SUCCESS
        }

        // always check for a draw
        val drawType = winDetection.checkDraw()

        if(drawType != null) {
            finish(Draw(drawType))
        }

        return MoveResult.SUCCESS
    }

    abstract fun start()

}