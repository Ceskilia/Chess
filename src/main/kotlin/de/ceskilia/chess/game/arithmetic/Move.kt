package de.ceskilia.chess.game.arithmetic

import de.ceskilia.chess.game.piece.ChessPiece

data class Move(
    val chessPiece: ChessPiece,
    val startPosition: Position,
    val newPosition: Position,
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
                newPosition.toString() +
                (if (isCheck) CHECK_NOTATION else "")
    }

}