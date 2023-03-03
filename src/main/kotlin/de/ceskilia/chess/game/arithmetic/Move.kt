package de.ceskilia.chess.game.arithmetic

import de.ceskilia.chess.game.piece.ChessPiece

data class Move(
    val chessPiece: ChessPiece,
    val startPosition: Position,
    val endPosition: Position,
    val isCheck: Boolean,
    val capturedPiece: ChessPiece? = null
) {

    companion object {

        const val CAPTURE_NOTATION = 'x'
        const val CHECK_NOTATION = '+'

    }

    fun isCapture(): Boolean = capturedPiece != null

    override fun toString(): String {
        // (if PAWN: use column/y, if same pieces can make same move insert ???)
        return chessPiece.type.notation.toString() +
                (if (isCapture()) CAPTURE_NOTATION else "") +
                endPosition.toString() +
                (if (isCheck) CHECK_NOTATION else "")
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