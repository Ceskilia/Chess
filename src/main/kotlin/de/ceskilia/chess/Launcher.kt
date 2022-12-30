package de.ceskilia.chess

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.Position

fun main() {

    val game = ChessGame()

    while (true) {
        println(game.chessboard.printBoard())
        println()
        println("Enter new position coordinates:")
        val start = Position(readln().toInt(), readln().toInt())
        val end = Position(readln().toInt(), readln().toInt())
        game.chessboard.getPieceAt(start)?.move(end)
        println()
    }

}