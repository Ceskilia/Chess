@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.piece.ChessPiece
import kotlin.math.ceil


class BoardHistory {

    val moves = mutableListOf<Move>()
    val capturedPieces = mutableListOf<ChessPiece>()

    fun currentMove(): Int {
        return ceil(moves.size / 2.0).toInt()
    }

    fun hasLastMove(): Boolean = moves.isNotEmpty()

    fun lastMove(): Move? = if (hasLastMove()) moves.last() else null

    fun lastMoves(amount: Int): List<Move> {
        return moves.takeLast(amount)
    }

}