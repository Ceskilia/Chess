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

    abstract val players: List<Player>
    abstract val board: Chessboard

    val history = GameHistory()
    var result: GameResult<*>? = null
        private set
    lateinit var currentTurn: Player
        protected set

    protected val winDetection by lazy { WinDetection(this) }

    fun setup() {
        this.currentTurn = players.first()
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

    fun processMoves(moves: List<Move>) {
        moves.forEach {
            move(it.startPosition, it.endPosition)
        }
    }

    open fun move(startPosition: Position, endPosition: Position): MoveResult {
        val piece = board.pieceAt(startPosition) ?: return MoveResult.INVALID_POSITION

        if (!currentTurn.isOwnPiece(piece)) {
            return MoveResult.WRONG_COLOR
        }

        val move = currentTurn.move(piece, endPosition) ?: return MoveResult.INVALID_MOVE
        (history.moves as MutableList).add(move)
        (history.encodedTableStates as MutableList).add(board.encodeCurrentState())
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