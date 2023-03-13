@file:Suppress("MemberVisibilityCanBePrivate")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.game.piece.ChessPiece
import kotlin.math.ceil

class GameHistory {

    val moves: List<Move> = mutableListOf()
    val encodedTableStates: List<Long> = mutableListOf()

    fun capturedPieces(): List<ChessPiece> {
        return moves.filter(Move::isCapture).map { it.capturedPiece!! }
    }

    fun movesOf(piece: ChessPiece): List<Move> {
        return moves.filter { it.chessPiece == piece }
    }

    fun currentMove(): Int {
        return ceil(moves.size / 2.0).toInt()
    }

    fun hasLastMove(): Boolean = moves.isNotEmpty()

    fun lastMove(): Move? = if (hasLastMove()) moves.last() else null

    fun lastMoves(amount: Int): List<Move> {
        return moves.takeLast(amount)
    }

    override fun toString(): String {
        return moves.chunked(2)
            .mapIndexed { index, moves -> "${index + 1}. ${moves.joinToString(" ")}" }
            .joinToString("\n")
    }

}