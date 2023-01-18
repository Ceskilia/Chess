@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.player

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.piece.BlockableChessPiece
import de.ceskilia.chess.game.piece.ChessPiece

data class Player(
    val game: ChessGame,
    val name: String,
    val pieceColor: ChessPiece.Color
) {

    fun getPieces(): List<ChessPiece> {
        return game.board.getPieces(pieceColor)
    }

    fun getOpponentPieces(): List<ChessPiece> {
        return game.board.getPieces().filter { it.color != this.pieceColor }
    }

    fun getKing(): ChessPiece {
        return getPieces().first { it.type == ChessPiece.Type.KING }
    }

    fun isOwnPiece(chessPiece: ChessPiece): Boolean {
        return getPieces().contains(chessPiece)
    }

    fun isChecked(): Boolean {
        return getOpponentPieces().any() { it.canMoveTo(getKing().position) }
    }

    fun canMoveTo(piece: ChessPiece, position: Position): Boolean {
        return calculateMoves()[piece]?.contains(position) ?: false
    }

    fun hasMoves(): Boolean {
        return calculateMoves().isNotEmpty()
    }

    fun calculateMoves(): Map<ChessPiece, Set<Position>> {
        val king = getKing()
        val checkingPieces = getOpponentPieces().filter { it.canMoveTo(king.position) }

        if (checkingPieces.isNotEmpty()) {
            val moves = mutableMapOf<ChessPiece, Set<Position>>()

            // the moves the king can make to move out of check or to eventually take the checking piece
            moves[king] = king.calculateMoves()

            // only 1 piece giving check can be taken or blocked
            if (checkingPieces.size == 1) {
                val checkingPiece = checkingPieces.first()

                getPieces().filter { it != king }.forEach {

                    // check if piece can take
                    if (it.canMoveTo(checkingPiece.position)) {
                        moves[it] = setOf(checkingPiece.position)
                    }

                    if(checkingPiece !is BlockableChessPiece) {
                        return@forEach
                    }

                    // check if piece can block
                    for (line in checkingPiece.calculateLines()) {

                        // check if this is the checking line
                        if (!line.contains(king.position)) {
                            continue
                        }

                        for (position in line) {
                            if (it.canMoveTo(position)) {
                                moves[it] = moves.getOrDefault(it, setOf()) + position
                            }
                        }

                    }

                }

            }

            return moves
        }

        return getPieces().associateWith { it.calculateMoves() }
    }

}