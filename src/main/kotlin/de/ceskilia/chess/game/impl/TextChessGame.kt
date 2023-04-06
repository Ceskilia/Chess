package de.ceskilia.chess.game.impl

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.impl.DefaultChessboard
import de.ceskilia.chess.game.piece.ChessPiece
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.MoveResult

class TextChessGame : ChessGame() {

    override val board = DefaultChessboard(this)
    override val players = requestPlayers()
    override var currentTurn = players.first()

    init {
        board.setup()
    }

    override fun start() {
        while (!isFinished()) {

            println(board)
            println()
            println("Enter new position coordinates:")
            println(currentTurn.calculateMoves().map { it.key.notation + "-" + it.value })

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

    private fun requestPlayers(): List<Player> {
        // maybe do some requesting of names etc.
        return listOf(
            Player(this, "", ChessPiece.Color.WHITE),
            Player(this, "", ChessPiece.Color.BLACK)
        )
    }

}