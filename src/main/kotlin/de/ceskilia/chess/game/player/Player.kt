@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game.player

import de.ceskilia.chess.game.ChessGame
import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.arithmetic.Position
import de.ceskilia.chess.game.board.Chessboard
import de.ceskilia.chess.game.result.Win
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractKing
import de.ceskilia.chess.piece.variation.Blockable

open class Player(
    val game: ChessGame,
    val name: String,
    val pieceColor: ChessPiece.Color,
    startTime: Int
) {

    val isActive: Boolean
        get() = game.activePlayers.contains(this)

    val timer: Timer = Timer.of(
        initialTime = startTime,
        action = { game.winDetection.checkTimeOf(this) }
    )

    fun pieces(): List<ChessPiece> {
        return game.board.pieces(pieceColor)
    }

    fun opponents(): List<Player> {
        return game.players.filter { it != this }
    }

    fun opponentPieces(): List<ChessPiece> {
        return game.board.opponentPieces(pieceColor)
    }

    fun valueOfPieces(): Int {
        return game.board.valueOf(this.pieceColor)
    }

    fun ownKing(): AbstractKing {
        return pieces()
            .filterIsInstance<AbstractKing>()
            .first()
    }

    fun isOwnPiece(chessPiece: ChessPiece): Boolean {
        return pieces().contains(chessPiece)
    }

    fun isStalemated(): Boolean {
        return !hasMoves() && !isChecked()
    }

    fun isChecked(): Boolean {
        return ownKing().isChecked()
    }

    fun isChecking(player: Player): Boolean {
        val king = player.ownKing()
        return pieces().any { it.canMoveTo(king.position) }
    }

    fun isCheckmated(): Boolean {
        return !hasMoves() && isChecked()
    }

    fun isCheckmating(player: Player): Boolean {
        return isChecking(player) && !player.hasMoves()
    }

    fun canCastle(side: Chessboard.Side): Boolean {
        return ownKing().checkCastle(side).isNotEmpty()
    }

    open fun hasSufficientMaterial(): Boolean {
        return pieces().size > 1
    }

    fun move(piece: ChessPiece, endPosition: Position): Move? {
        return if (canMoveTo(piece, endPosition)) game.board.move(piece, endPosition) else null
    }

    fun canMoveTo(piece: ChessPiece, position: Position): Boolean {
        return calculateMoves()[piece]?.contains(position) ?: false
    }

    fun hasMoves(): Boolean {
        val moves = calculateMoves()
        return moves.isNotEmpty() && moves.values.any { it.isNotEmpty() }
    }

    fun calculateMoves(): Map<ChessPiece, Set<Position>> {
        val king = ownKing()
        val checkingPieces = opponentPieces().filter { it.canMoveTo(king.position) }

        if (checkingPieces.isNotEmpty()) {
            val moves = mutableMapOf<ChessPiece, Set<Position>>()

            // the moves the king can make to move out of check or to eventually take the checking piece
            moves[king] = king.calculateMoves()

            // only 1 piece giving check can be taken or blocked
            if (checkingPieces.size == 1) {
                val checkingPiece = checkingPieces.first()

                pieces().filter { !it.isPinned() }.filter { it != king }.forEach {

                    // check if piece can take
                    if (it.canMoveTo(checkingPiece.position)) {
                        moves[it] = setOf(checkingPiece.position)
                    }

                    // check if piece can be blocked
                    if (checkingPiece !is Blockable) {
                        return@forEach
                    }

                    val blockingMoves = it.calculateBlockingMoves(checkingPiece, king)

                    moves.compute(it) { _, currentMoves ->
                        if (currentMoves.isNullOrEmpty()) blockingMoves
                        else currentMoves + blockingMoves
                    }

                }

            }

            return moves
        }


        return pieces().associateWith(ChessPiece::calculateMoves)
    }

    fun resign() {
        game.removePlayer(this, Win.Reason.RESIGNATION)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (game != other.game) return false
        if (name != other.name) return false
        if (pieceColor != other.pieceColor) return false
        if (timer != other.timer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = game.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + pieceColor.hashCode()
        result = 31 * result + timer.hashCode()
        return result
    }

}