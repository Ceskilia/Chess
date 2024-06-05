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
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.variation.Checkable
import de.ceskilia.chess.util.calculateFEN
import de.ceskilia.chess.util.calculateUniqueFEN

abstract class ChessGame {

    abstract val winDetection: WinDetection
    abstract val board: Chessboard
    abstract val players: List<Player>
    abstract var currentTurn: Player
        protected set

    val history: GameHistory = GameHistory()
    var state: State = State.PENDING
        protected set
    var result: GameResult? = null
        private set

    val activePlayers: List<Player> by lazy { players.toMutableList() }

    fun removePlayer(player: Player, reason: Win.Reason) {
        if (isFinished()) {
            return
        }

        (activePlayers as MutableList).remove(player)

        if (activePlayers.size != 1) {
            return
        }

        finish(Win(activePlayers.first(), reason))
    }

    fun shuffleTurn() {
        val currentIndex = activePlayers.indexOf(currentTurn)
        this.currentTurn = activePlayers[
            if (currentIndex == activePlayers.lastIndex) 0
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

        currentTurn.timer.pause()
        shuffleTurn()
        currentTurn.timer.start()

        saveCurrentBoardState()

        // if check, check if mate
        if (move.isCheck) {
            val matedPlayers = winDetection.checkCheckmate()
            matedPlayers.forEach { removePlayer(it, Win.Reason.CHECKMATE) }

            if (isFinished()) {
                return MoveResult.SUCCESS
            }

        }

        // always check for a draw
        val drawType = winDetection.checkDraw()

        if (drawType != null) {
            finish(Draw(drawType))
        }

        return MoveResult.SUCCESS
    }

    fun start() {
        if (isRunning())
            return
        this.state = State.RUNNING

        saveCurrentBoardState()
        validate()
        onStart()
    }

    fun isRunning(): Boolean = this.state == State.RUNNING

    fun finish(result: GameResult) {
        if (isFinished())
            return
        this.result = result
        this.state = State.FINISHED

        onFinish()
    }

    fun isFinished(): Boolean = this.state == State.FINISHED

    open fun calculateCurrentFEN(): String {
        return calculateFEN(this)
    }

    protected open fun onStart() {}

    protected open fun onFinish() {}

    private fun saveCurrentBoardState() {
        (history.encodedTableStates as MutableList).add(calculateUniqueFEN(this))
    }

    private fun validate() {
        val checkable = board.pieces()
            .filterIsInstance<Checkable>()
            .distinctBy(ChessPiece::color)

        check(checkable.size == activePlayers.size) { "There must be exactly 1 king for each player on the board." }
    }

    enum class State {

        PENDING,
        RUNNING,
        FINISHED

    }

}