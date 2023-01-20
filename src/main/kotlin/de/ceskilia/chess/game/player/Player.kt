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

    fun getOpponents(): List<Player> {
        return game.players.filter { it != this }
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

    fun isStalemated(): Boolean {
        return isChecked() && !hasMoves()
    }

    fun isChecked(): Boolean {
        return getOpponentPieces().any { it.canMoveTo(getKing().position) }
    }

    fun isChecking(player: Player): Boolean {
        return getPieces().any { it.canMoveTo(player.getKing().position) }
    }

    fun isCheckedBy(player: Player): Boolean {
        return player.isChecking(this)
    }

    fun isCheckmated(): Boolean {
        return isChecked() && !hasMoves()
    }

    fun isCheckmating(player: Player): Boolean {
        return isChecking(player) && !player.hasMoves()
    }

    fun isCheckmatedBy(player: Player): Boolean {
        return isCheckedBy(player) && !hasMoves()
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

                    // check if piece can be blocked
                    if (checkingPiece !is BlockableChessPiece) {
                        return@forEach
                    }

                    val blockingMoves = it.calculateBlockableMoves(checkingPiece, king)

                    moves.compute(it) { _, currentMoves ->
                        if (currentMoves.isNullOrEmpty()) blockingMoves
                        else currentMoves + blockingMoves
                    }

                }

            }

            return moves
        }

        return getPieces().associateWith { it.calculateMoves() }
    }

}