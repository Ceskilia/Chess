@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.DefaultChessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.result.GameResult
import de.ceskilia.chess.game.result.MoveResult
import de.ceskilia.chess.game.result.Win
import de.ceskilia.chess.game.windetection.WinDetection

class ChessGame {

    private val moves = mutableListOf<Move>()
    private val winDetection = WinDetection(this)

    val history = GameHistory(moves)
    val board = DefaultChessboard(this)
    val players = listOf(
        Player(this, "", ChessPiece.Color.WHITE),
        Player(this, "", ChessPiece.Color.BLACK)
    )

    var result: GameResult<*>? = null
        private set
    var currentTurn: Player = players.first()
        private set

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

    fun move(startPosition: Position, endPosition: Position): MoveResult {
        val piece = board.getPieceAt(startPosition) ?: return MoveResult.INVALID_POSITION

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

    fun start() {
        while (!isFinished()) {

            println(board)
            println()
            println("Enter new position coordinates:")
            println(currentTurn.calculateMoves().map { it.key.type.notation + "-" + it.value })

            val (start, end) = readln().chunked(2).map(Position.Companion::fromNotation)

            when(move(start, end)) {
                MoveResult.INVALID_POSITION -> println("No Piece")
                MoveResult.WRONG_COLOR -> println("Wrong color")
                MoveResult.INVALID_MOVE -> println("Cant move there")
                MoveResult.SUCCESS -> {
                    // play sound
                }
            }

        }

        println(board)
        println(history)
    }

}