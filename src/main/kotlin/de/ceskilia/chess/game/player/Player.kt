package de.ceskilia.chess.game.player

import de.ceskilia.chess.game.ChessPiece

data class Player(
    val name: String,
    val pieceColor: ChessPiece.Color
) {

}