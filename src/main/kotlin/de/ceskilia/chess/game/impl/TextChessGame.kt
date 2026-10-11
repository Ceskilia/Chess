package de.ceskilia.chess.game.impl

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.impl.DefaultChessboard
import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.game.result.Draw
import de.ceskilia.chess.game.result.MoveResult
import de.ceskilia.chess.game.result.Win
import de.ceskilia.chess.game.windetection.impl.DefaultWinDetection
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.util.clearConsole
import de.ceskilia.chess.util.formatChessPieceColor
import de.ceskilia.chess.util.formatPlayerColor
import org.fusesource.jansi.AnsiConsole
import kotlin.system.exitProcess


class TextChessGame : ChessGame() {

    companion object {

        val INPUT_FORMAT = "([a-zA-Z][0-9]){2}".toRegex()

    }

    override val winDetection = DefaultWinDetection(this)
    override val board = DefaultChessboard(this)
    override val players = requestPlayers()
    override var currentTurn = players.first()

    init {
        board.setup()
    }

    // maybe start with short delay
    override fun onStart() {
        AnsiConsole.systemInstall()

        clearConsole()
        currentTurn.timer.start()
        gameLoop()
    }

    override fun onFinish() {
        clearConsole()
        printResult()

        AnsiConsole.systemUninstall()
        exitProcess(0)
    }

    private fun gameLoop() {
        while (!isFinished()) {
            printScreen()

            val input = readln()

            if (!INPUT_FORMAT.matches(input)) {
                clearConsole()
                println(input)
                println("Invalid input format")
                continue
            }

            val (start, end) = input.chunked(2).map(Position.Companion::fromNotation)

            clearConsole()

            when (move(start, end)) {
                MoveResult.INVALID_POSITION -> {
                    println(input)
                    println("No Piece")
                }
                MoveResult.WRONG_COLOR -> {
                    println(input)
                    println("Wrong color")
                }
                MoveResult.INVALID_MOVE -> {
                    println(input)
                    println("Cant move there")
                }
                MoveResult.SUCCESS -> {
                    // play sound
                }
            }
        }
    }

    private fun requestPlayers(): List<Player> {
        // maybe do some requesting of names etc.
        return listOf(
            Player(this, "White", ChessPiece.Color.WHITE, 60),
            Player(this, "Black", ChessPiece.Color.BLACK, 60)
        )
    }

    private fun printScreen() {
        val preparation =
            if (currentTurn.timer.isPreparation) ", ${currentTurn.timer.preparationTime}s preparation"
            else ""

        println()
        println(formatBoard())
        println()
        println("${formatPlayerColor(currentTurn)}'s turn (${currentTurn.timer.time}s left$preparation)")
        println()
        println("Enter new position coordinates:")
        println(currentTurn.calculateMoves()
            .filter { it.value.isNotEmpty() }
            .map { it.key.notation + "-" + it.value }
            .sorted()
            .joinToString()
        )
    }

    private fun printResult() {
        println()
        println(formatBoard())
        println()
        println(formatResult())
        println()
        println(history)
    }

    private fun formatBoard(): String {
        return with(board) {
            buildString {

                for (row in 0 until size) {
                    append("  ")
                    append("+---".repeat(size) + '+')
                    append("\n")
                    append("${row + 1} ")

                    for (column in 0 until size) {
                        val piece = pieceAt(column, row)

                        append("| ${formatChessPieceColor(piece) ?: " "} ")
                    }

                    append("|")
                    append("\n")
                }

                append("  ")
                append("+---".repeat(size) + '+')
                append("\n")
                append("  ")

                for (column in 0 until size) {
                    append(" ${Position.coordinateToLetter(column)}  ")
                }

            }
        }
    }

    private fun formatResult(): String {
        if (result is Win) {
            val win = result as Win

            return "${formatPlayerColor(win.player)} won (by ${win.reason})"
        }

        if (result is Draw) {
            val draw = result as Draw
            return "Draw (by ${draw.type})"
        }

        throw IllegalStateException("Cannot format result since no result is defined yet.")
    }

}