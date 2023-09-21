package de.ceskilia.chess.game.arithmetic

import de.ceskilia.chess.piece.ChessPiece
import de.ceskilia.chess.piece.standardtype.AbstractPawn
import de.ceskilia.chess.piece.variation.Checkable
import de.ceskilia.chess.util.appendIf

data class Move(
    val chessPiece: ChessPiece,
    val capturedPiece: ChessPiece? = null,
    val startPosition: Position,
    val endPosition: Position,
    val isCheck: Boolean,
) {

    val isCapture: Boolean = capturedPiece != null

    init {
        check(capturedPiece !is Checkable) { "Captured piece cannot be a checkable! ($this)" }
    }

    companion object {

        const val CAPTURE_NOTATION = 'x'
        const val CHECK_NOTATION = '+'

    }

    override fun toString(): String {
        val result = StringBuilder()

        val isPawn = chessPiece is AbstractPawn
        val movedStraightForward = endPosition.x == startPosition.x

        if (!(isPawn && movedStraightForward)) {
            result.append(chessPiece.notation.toString())
        }

        return result
            .appendIf(CAPTURE_NOTATION, this::isCapture)
            .append(endPosition.toString())
            .appendIf(CHECK_NOTATION, this::isCheck)
            .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Move

        if (chessPiece != other.chessPiece) return false
        if (startPosition != other.startPosition) return false
        if (endPosition != other.endPosition) return false
        if (isCheck != other.isCheck) return false
        if (capturedPiece != other.capturedPiece) return false

        return true
    }

    override fun hashCode(): Int {
        var result = chessPiece.hashCode()
        result = 31 * result + startPosition.hashCode()
        result = 31 * result + endPosition.hashCode()
        result = 31 * result + isCheck.hashCode()
        result = 31 * result + (capturedPiece?.hashCode() ?: 0)
        return result
    }

}