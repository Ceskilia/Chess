@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package de.ceskilia.chess.game

import de.ceskilia.chess.game.arithmetic.Move
import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import kotlin.math.ceil

class GameHistory {

    val moves: List<Move> = mutableListOf()
    val encodedTableStates: List<String> = mutableListOf()

    fun ChessPiece.isCaptured(): Boolean {
        return moves.any { it.capturedPiece === this }
    }

    fun capturedPieces(): List<ChessPiece> {
        return moves.filter(Move::isCapture).map { it.capturedPiece!! }
    }

    fun movesOf(piece: ChessPiece): List<Move> {
        return moves.filter { it.chessPiece == piece }
    }

    fun currentMoveCount(): Int {
        return ceil(moves.size / 2.0).toInt()
    }

    fun nextMoveCount(): Int {
        return (moves.size / 2) + 1
    }

    fun hasThreefoldRepetition(): Boolean {
        if (moves.size < 8) {
            return false
        }

        return encodedTableStates.map { state -> encodedTableStates.count { it == state } }
            .distinct()
            .any { it >= 3 }
    }

    // these moves are consecutive
    fun hasFiftyInsignificantMoves(): Boolean {
        if (moves.size < 50) {
            return false
        }

        val lastFiftyMoves = lastMoves(50)
        return lastFiftyMoves.none { it.chessPiece is AbstractPawn } && lastFiftyMoves.none(Move::isCapture)
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