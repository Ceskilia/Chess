package de.ceskilia.chess.util

import de.ceskilia.chess.game.player.Player
import de.ceskilia.chess.piece.ChessPiece

const val ESCAPE = "\u001B"
const val CLEAR = "$ESCAPE[2J$ESCAPE[H"
const val RESET = "$ESCAPE[0m"
const val BLACK_COLOR = "$ESCAPE[36m"
const val WHITE_COLOR = "$ESCAPE[97m"

fun clearConsole() {
    print(CLEAR)
    System.out.flush()
}

fun consoleColorOf(color: ChessPiece.Color?): String {
    return when (color) {
        ChessPiece.Color.BLACK -> BLACK_COLOR
        ChessPiece.Color.WHITE -> WHITE_COLOR
        null -> ""
    }
}

fun formatPlayerColor(player: Player): String {
    return "${consoleColorOf(player.pieceColor)}${player.name}$RESET"
}

fun formatChessPieceColor(piece: ChessPiece?): String? {
    return if (piece != null) "${consoleColorOf(piece.color)}${piece.notation}$RESET" else null
}